package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

/**
 * Ebook scraper interface.
 * Sources define which elements have to be scrapped.
 * @author Nicolas
 */
public interface EbookScraper {

    /**
     * Parse HTML document.
     * @param url
     * @return HTML document
     * @throws IOException
     */
    Document parseHTMLDocument(String url) throws IOException;

    /**
     * Parse author.
     * @param document
     * @return author
     */
    String parseAuthor(Document document);

    /**
     * Parse story title.
     * @param document
     * @return title
     */
    String parseStoryTitle(Document document);

    /**
     * Checks if story has volumes.
     * @param document
     * @return true if has volumes
     */
    boolean hasVolumes(Document document);

    /**
     * Parse volume titles.
     * @param document
     * @return list of volume titles
     */
    List<String> parseVolumeTitles(Document document);

    /**
     * Parse chapter title.
     * @param document
     * @return title
     */
    String parseChapterTitle(Document document);

    /**
     * Parse chapter text.
     * @param document
     * @return text
     */
    String parseChapterText(Document document);

    /**
     * Parse chapter urls.
     * @param document
     * @return chapter urls
     */
    List<String> parseAllChapterUrls(Document document);

    /**
     * Sort chapters by volume.
     * @param document
     * @return chapters sorted by volume
     */
    Map<Integer, List<String>> sortChaptersByVolume(Document document);

    /**
     * Parse chapter number.
     * @param document
     * @return chapter number
     */
    Integer parseChapterNumber(Document document);

}
