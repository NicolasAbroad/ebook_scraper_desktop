package com.nicolas_abroad.epub_scraper_desktop.ebook;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for a single volume.
 *
 * @author Nicolas
 */
public class Volume {

	private EbookScraper scraper;

	private List<String> chapterUrls;

	private List<Chapter> chapters = new ArrayList<>();

	private String author;

	private String title;

	private String volumeNumberFormatted;

	/**
	 * Create a new volume with a scraper and a list of chapter urls.
	 *
	 * @param scraper
	 * @param chapterUrls
	 */
	public Volume(EbookScraper scraper, List<String> chapterUrls) {
		this.scraper = scraper;
		this.chapterUrls = chapterUrls;
	}

	/**
	 * Get author.
	 *
	 * @return author
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Set author.
	 *
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Get title.
	 *
	 * @return title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Set title.
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get list of all chapters.
	 *
	 * @return list of all chapters
	 */
	public List<Chapter> getChapters() {
		return chapters;
	}

	/**
	 * Get formatted volume number with leading zeros.
	 *
	 * @return volume number
	 */
	public String getVolumeNumber() {
		return volumeNumberFormatted;
	}

	/**
	 * Set formatted volume number with leading zeros.
	 *
	 * @param volumeNumber
	 */
	public void setVolumeNumber(String volumeNumber) {
		this.volumeNumberFormatted = volumeNumber;
	}

	/**
	 * Scrape all chapters in the chapter urls list.
	 *
	 * @throws IOException
	 */
	public void generate() throws Exception {
		for (String url : chapterUrls) {
			Chapter chapter = new Chapter(scraper, url);
			chapter.generate();
			this.chapters.add(chapter);
		}
	}

}
