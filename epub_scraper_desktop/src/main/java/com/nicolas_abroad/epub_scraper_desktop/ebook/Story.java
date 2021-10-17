package com.nicolas_abroad.epub_scraper_desktop.ebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;

/**
 * Class for entire story.
 * @author Nicolas
 */
public class Story {

    private EbookScraper scraper;

    private String url;

    private Map<Integer, List<String>> volumeUrls;

    private List<Volume> volumes = new ArrayList<Volume>();

    private static String TITLE_CLEAN_REGEX = "[<>:\"/\\\\|?*]";

    /**
     * Create a new story with a scraper and a url.
     * @param scraper
     * @param indexUrl
     */
    public Story(EbookScraper scraper, String indexUrl) {
        this.scraper = scraper;
        this.url = indexUrl;
    }

    /**
     * Parse chapter urls by volume title.
     * @param document HTML document
     */
    private void parseVolumeUrls(Document document) {
        if (scraper.hasVolumes(document)) {
            this.volumeUrls = scraper.parseChapterUrlsByVolume(document);
        } else {
            this.volumeUrls = new HashMap<Integer, List<String>>();
            this.volumeUrls.put(1, scraper.parseAllChapterUrls(document));
        }
    }

    /**
     * Populate volumes list with unscrapped volume urls.
     */
    private void populateVolumes() {
        for (Integer key : volumeUrls.keySet()) {
            Volume volume = new Volume(scraper, volumeUrls.get(key));
            this.volumes.add(volume);
        }
    }

    /**
     * Assign each volume its formatted volume number.
     */
    private void assignVolumeNumbers() {
        int numberZeros = String.valueOf(volumes.size()).length();
        for (int i = 0; i < volumes.size(); i++) {
            volumes.get(i).setVolumeNumber(String.format("%0" + numberZeros + "d", i + 1));
        }
    }

    /**
     * Assign each volume its title.
     * @param document HTML document
     */
    private void assignVolumeTitles(Document document) {
        List<String> volumeTitles = scraper.parseVolumeTitles(document);

        if (volumeTitles == null || volumeTitles.isEmpty()) {
            // if no volumes exist on index page
            Volume volume = this.volumes.get(0);
            volume.setTitle(scraper.parseStoryTitle(document));
        } else {
            // if volumes exist on index page
            for (int i = 0; i < volumeTitles.size(); i++) {
                // Get title & clean out unwanted characters
                String title = volumeTitles.get(i);
                String cleanTitle = title.replaceAll(TITLE_CLEAN_REGEX, "");

                // Set volume title
                Volume volume = this.volumes.get(i);
                volume.setTitle(volume.getVolumeNumber() + " - " + cleanTitle);
            }
        }
    }

    /**
     * Assign author to volume.
     * @param document HTML document
     */
    private void assignVolumeAuthor(Document document) {
        String author = scraper.parseAuthor(document);
        for (int i = 0; i < volumes.size(); i++) {
            volumes.get(i).setAuthor(author);
        }
    }

    /**
     * Generate all volumes.
     * @throws IOException
     */
    public void generate() throws IOException {
        Document document = scraper.parseHTMLDocument(url);
        parseVolumeUrls(document);
        populateVolumes();
        assignVolumeNumbers();
        assignVolumeTitles(document);
        assignVolumeAuthor(document);
        for (Volume volume : volumes) {
            volume.generate();
        }
    }

    /**
     * Get volumes.
     * @return volumes
     */
    public List<Volume> getVolumes() {
        return volumes;
    }

}
