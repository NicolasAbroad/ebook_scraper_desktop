package com.nicolas_abroad.epub_scraper_desktop.ebook;

import java.io.IOException;

import org.jsoup.nodes.Document;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;

/**
 * Class for a single chapter of a volume.
 * @author Nicolas
 */
public class Chapter {

    private EbookScraper scraper;

    private String url;

    private int chapterNumber;

    private String chapterTitle;

    private String text;

    /**
     * Create a new chapter with a scraper and a url.
     * @param scraper
     * @param url
     */
    public Chapter(EbookScraper scraper, String url) {
        this.scraper = scraper;
        this.url = url;
    }

    /**
     * Get chapter number.
     * @return chapter number
     */
    public int getChapterNumber() {
        return this.chapterNumber;
    }

    /**
     * Set chapter number.
     * @param chapterNumber
     */
    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    /**
     * Get chapter title.
     * @return chapter title
     */
    public String getTitle() {
        return this.chapterTitle;
    }

    /**
     * Get text contained in chapter.
     * @return text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Scrape all relevant information for this chapter.
     * @throws IOException
     */
    public void generate() throws IOException {
        Document document = scraper.parseHTMLDocument(url);
        this.chapterTitle = scraper.parseChapterTitle(document);
        this.text = scraper.parseChapterText(document);
        this.chapterNumber = scraper.parseChapterNumber(document);
    }

}
