package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Ebook source: syosetsu.com
 * @author Nicolas
 */
public class SyosetsuSource implements EbookSource {

    private static final String AUTHOR_SELECTOR = ".novel_writername > a";

    private static final String STORY_TITLE_SELECTOR = "#novel_contents > #novel_color > p.novel_title";

    public Document parseHTMLDocument(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document;
    }

    public String parseAuthor(Document document) {
        return document.selectFirst(AUTHOR_SELECTOR).text();
    }

    public String parseStoryTitle(Document document) {
        return document.selectFirst(STORY_TITLE_SELECTOR).text();
    }

    public String parseVolumeTitle(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

    public String parseChapterTitle(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

    public String parseChapterText(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer parseChapterNumber(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

}
