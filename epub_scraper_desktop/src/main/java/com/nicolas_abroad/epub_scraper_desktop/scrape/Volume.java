package com.nicolas_abroad.epub_scraper_desktop.scrape;

import java.util.List;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;

/**
 * Interface for a single volume.
 * @author Nicolas
 */
public interface Volume {

    /**
     * Set chapter's source.
     * @param source
     */
    void setEbookSource(EbookScraper source);

    /**
     * Add a chapter to volume.
     * @param chapter
     */
    void addChapter(Chapter chapter);

    /**
     * Get author.
     * @return author
     */
    String getAuthor();

    /**
     * Get title.
     * @return title
     */
    String getTitle();

    /**
     * Get a list containing all chapters.
     * @return list of all chapters
     */
    List<Chapter> getChapters();
}
