package com.nicolas_abroad.epub_scraper_desktop.format;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;

/**
 * Interface used to generate ebooks, changing based on the format.
 * @author Nicolas
 */
public interface EbookFormat {

    /**
     * Generate ebook.
     * @param volume
     * @throws IOException
     */
    void generate(Volume volume) throws IOException;

}
