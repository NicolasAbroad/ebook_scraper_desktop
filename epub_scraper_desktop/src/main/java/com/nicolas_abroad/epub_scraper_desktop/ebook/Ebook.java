package com.nicolas_abroad.epub_scraper_desktop.ebook;

import java.util.List;

import com.nicolas_abroad.epub_scraper_desktop.scrape.Volume;

public interface Ebook {

    void setEbookFormat(EbookFormat EbookFormat);

    void generateEbooks(List<Volume> volumes);

}
