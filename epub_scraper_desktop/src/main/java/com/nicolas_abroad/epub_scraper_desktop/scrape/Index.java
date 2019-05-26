package com.nicolas_abroad.epub_scraper_desktop.scrape;

import java.util.List;
import java.util.Map;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookSource;

public interface Index {

    /**
     * Set chapter's source.
     * @param source
     */
    void setEbookSource(EbookSource source);

    /**
     * Get chapters urls by volume.
     * @param url
     * @return map of chapter urls (value) by volume number(key)
     */
    Map<Integer, List<String>> getChaptersUrlsByVolume(String url);

}
