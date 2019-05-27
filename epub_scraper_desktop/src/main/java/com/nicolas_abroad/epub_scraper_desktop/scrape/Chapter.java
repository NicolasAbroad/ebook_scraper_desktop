package com.nicolas_abroad.epub_scraper_desktop.scrape;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;

/**
 * Interface for a single chapter of a volume.
 * @author Nicolas
 */
public interface Chapter {

    /**
     * Set chapter's source.
     * @param source
     */
    void setEbookSource(EbookScraper source);

    /**
     * Get chapter number.
     * @return chapter number.
     */
    int getChapterNumber();

    /**
     * Set chapter number.
     * @param chapterNumber chapter number
     */
    void setChapterNumber(int chapterNumber);

    /**
     * Get text contained in chapter.
     * @return text
     */
    String getText();

    /**
     * Set text contained in chapter;
     * @param text
     */
    void setText(String text);

    // TODO add appendText?

}
