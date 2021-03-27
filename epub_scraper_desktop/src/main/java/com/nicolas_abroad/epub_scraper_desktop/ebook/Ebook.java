package com.nicolas_abroad.epub_scraper_desktop.ebook;

import java.util.List;

import com.nicolas_abroad.epub_scraper_desktop.format.EbookFormat;

/**
 * Interface for Ebooks
 * @author Nicolas
 */
public interface Ebook {

    /**
     * Set ebook's format.
     * @param EbookFormat
     */
    void setEbookFormat(EbookFormat EbookFormat);

    /**
     * Generate all ebooks.
     * @param url
     * @return list containing all ebooks
     */
    List<Volume> generateEbooks(String url);

}
