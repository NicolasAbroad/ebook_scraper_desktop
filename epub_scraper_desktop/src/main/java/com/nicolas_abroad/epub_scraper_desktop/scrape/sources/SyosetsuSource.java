package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Ebook source: syosetsu.com
 * @author Nicolas
 */
public class SyosetsuSource implements EbookSource {

    private static final String AUTHOR_SELECTOR = ".novel_writername > a";

    private static final String STORY_TITLE_SELECTOR = "#novel_contents > #novel_color > p.novel_title";

    private static final String CHAPTER_URLS_SELECTOR = "#novel_contents > div#novel_color > div.index_box > .novel_sublist2 > .subtitle > a";

    private static final String HREF_SELECTOR = "href";

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

    public List<String> parseAllChapterUrls(Document document) {
        List<String> urls = new ArrayList<>();
        Elements elements = document.select(CHAPTER_URLS_SELECTOR);
        elements.forEach(element -> {
            urls.add(element.attr(HREF_SELECTOR));
        });
        return urls;
    }

    public Map<String, List<String>> sortChaptersByVolume(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer parseChapterNumber(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

}
