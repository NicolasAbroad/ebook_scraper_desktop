package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import java.io.IOException;

import org.jsoup.nodes.Document;

/**
 * Ebook source interface.
 * Sources define which elements have to be scrapped.
 * @author Nicolas
 */
public interface EbookSource {

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
     * Parse volume title.
     * @param document
     * @return title
     */
    String parseVolumeTitle(Document document);

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
     * Parse chapter number.
     * @param document
     * @return chapter number
     */
    Integer parseChapterNumber(Document document);

}
