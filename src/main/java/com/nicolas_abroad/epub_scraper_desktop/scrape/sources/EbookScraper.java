package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Document;

/**
 * Ebook scraper interface.
 * Sources define which elements have to be scrapped.
 * @author Nicolas
 */
public abstract class EbookScraper {

    /**
     * Parse HTML document.
     * @param url
     * @return HTML document
     * @throws IOException
     */
    public abstract Document parseHTMLDocument(String url) throws IOException;

    /**
     * Parse author.
     * @param document
     * @return author
     */
    public abstract String parseAuthor(Document document);

    /**
     * Parse story title.
     * @param document
     * @return title
     */
    public abstract String parseStoryTitle(Document document);

    /**
     * Checks if story has volumes.
     * @param document
     * @return true if has volumes
     */
    public abstract boolean hasVolumes(Document document);

    /**
     * Parse volume titles.
     * @param document
     * @return list of volume titles
     */
    public abstract List<String> parseVolumeTitles(Document document);

    /**
     * Parse chapter title.
     * @param document
     * @return title
     */
    public abstract String parseChapterTitle(Document document);

    /**
     * Parse chapter text.
     * @param document
     * @return text
     */
    public abstract String parseChapterText(Document document);

    /**
     * Parse chapter urls. Used if story has no volumes.
     * @param document
     * @return chapter urls
     */
    public abstract List<String> parseAllChapterUrls(Document document);

    /**
     * Parse chapters by volume. Used if story has volumes.
     * @param document
     * @return chapters sorted by volume
     */
    public abstract Map<Integer, List<String>> parseChapterUrlsByVolume(Document document);

    /**
     * Parse chapter number.
     * @param document
     * @return chapter number
     */
    public abstract int parseChapterNumber(Document document);

    static String cleanChapterTitle(String title) {
        return StringEscapeUtils.escapeHtml4(title);
    }

    static String cleanChapterText(String text) {
        text = text.replaceAll("\u00a0", "");
        text = text.replaceAll("<br>", "<br></br>");
        text = text.replaceAll("<img>", "<img></img>");
        text = text.replaceAll("border[\\w\\W].*?\"[\\w\\W]*?\"", "");
        text = text.replaceAll("<img(.*?)>", "<img$1></img>");
        return text;
    }

}
