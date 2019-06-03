package com.nicolas_abroad.epub_scraper_desktop.ebook;

import com.nicolas_abroad.epub_scraper_desktop.format.EbookFormat;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;

public interface ScrapeElement {

    /**
     * Set the scraper used by this element.
     * @param scraper
     */
    void setScraper(EbookScraper scraper);

    /**
     * Set format used by this element.
     * @param format
     */
    void setFormat(EbookFormat format);

    /**
     * Generate this element.
     * @return scraped element
     */
    ScrapeElement generate();

}
