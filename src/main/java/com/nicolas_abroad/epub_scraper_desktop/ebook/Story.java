package com.nicolas_abroad.epub_scraper_desktop.ebook;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class for entire story.
 *
 * @author Nicolas
 */
public class Story {

	private EbookScraper scraper;

	private String url;

	private Map<Integer, List<String>> volumeUrls;

	private List<Volume> volumes = new ArrayList<>();

	private static String TITLE_CLEAN_REGEX = "[<>:\"/\\\\|?*&]";

	/**
	 * Create a new story with a scraper and a url.
	 *
	 * @param scraper
	 * @param indexUrl
	 */
	public Story(EbookScraper scraper, String indexUrl) {
		this.scraper = scraper;
		this.url = indexUrl;
	}

	/**
	 * Parse chapter urls by volume title.
	 *
	 * @param document HTML document
	 * @throws Exception
	 */
	private void parseVolumeUrls(Document document) throws Exception {
		if (scraper.hasVolumes(document)) {
			this.volumeUrls = scraper.parseChapterUrlsByVolume(document);
		} else {
			this.volumeUrls = new HashMap<>();
			this.volumeUrls.put(1, scraper.parseAllChapterUrls(document));
		}
	}

	/** Populate volumes list with unscrapped volume urls. */
	private void populateVolumes() {
		for (Integer key : volumeUrls.keySet()) {
			Volume volume = new Volume(scraper, volumeUrls.get(key));
			this.volumes.add(volume);
		}
	}

	/** Assign each volume its formatted volume number. */
	private void assignVolumeNumbers() {
		int numberZeros = String.valueOf(volumes.size()).length();
		for (int i = 0; i < volumes.size(); i++) {
			volumes.get(i).setVolumeNumber(String.format("%0" + numberZeros + "d", i + 1));
		}
	}

	/**
	 * Assign each volume its title.
	 *
	 * @param document HTML document
	 * @throws Exception
	 */
	private void assignVolumeTitles(Document document) throws Exception {
		List<String> volumeTitles = scraper.parseVolumeTitles(document);

		if (volumeTitles == null || volumeTitles.isEmpty()) {
			// if no volumes exist on index page
			Volume volume = this.volumes.get(0);

			// Get title & clean out unwanted characters
			String title = scraper.parseStoryTitle(document);
			title = title.replaceAll(TITLE_CLEAN_REGEX, "");

			// Set volume title
			volume.setTitle(title);
		} else {
			// if volumes exist on index page
			if (this.volumes.size() != volumeTitles.size()) {
				// Assign name to the first volume if none is assigned
				// In Kakuyomu the first volume can not have a name assigned
				String title = "";
				volumeTitles.add(0, title);
			}

			for (int i = 0; i < volumeTitles.size(); i++) {
				// Get title & clean out unwanted characters
				String title = volumeTitles.get(i);
				title = title.replaceAll(TITLE_CLEAN_REGEX, "");

				// Set volume title
				Volume volume = this.volumes.get(i);
				String finalTitle = title.isBlank() ? "" : (" - " + title);
				volume.setTitle(volume.getVolumeNumber() + finalTitle);
			}
		}
	}

	/**
	 * Assign author to volume.
	 *
	 * @param document HTML document
	 * @throws Exception
	 */
	private void assignVolumeAuthor(Document document) throws Exception {
		String author = scraper.parseAuthor(document);
		for (int i = 0; i < volumes.size(); i++) {
			volumes.get(i).setAuthor(author);
		}
	}

	/** Assign each chapter its number, if chapter number was not scraped. */
	private void assignNonScrapedChapterNumbers() {
		List<Chapter> chapters = volumes.stream().map(v -> v.getChapters()).flatMap(List::stream)
				.collect(Collectors.toList());

		// Check if chapter numbers were scraped
		long count = chapters.stream().filter(c -> c.getChapterNumber() == -1).count();
		if (chapters.size() != count) {
			return;
		}

		// Assign chapter numbers
		for (int i = 0; i < chapters.size(); i++) {
			Chapter chapter = chapters.get(i);
			if (chapter.getChapterNumber() == -1) {
				chapter.setChapterNumber(i + 1);
			}
		}
	}

	/**
	 * Generate all volumes.
	 *
	 * @throws Exception
	 */
	public void generate() throws Exception {
		Document document = scraper.parseHTMLDocument(url);
		parseVolumeUrls(document);
		populateVolumes();
		assignVolumeNumbers();
		assignVolumeTitles(document);
		assignVolumeAuthor(document);
		for (Volume volume : volumes) {
			volume.generate();
		}
		assignNonScrapedChapterNumbers();
	}

	/**
	 * Get volumes.
	 *
	 * @return volumes
	 */
	public List<Volume> getVolumes() {
		return volumes;
	}

}
