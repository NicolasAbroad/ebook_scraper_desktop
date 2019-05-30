package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Ebook scraper for syosetsu.com
 * @author Nicolas
 */
public class SyosetsuScraper implements EbookScraper {

    private static final String AUTHOR_SELECTOR = "div.novel_writername > a";

    private static final String STORY_TITLE_SELECTOR = "div#novel_contents > div#novel_color > p.novel_title";

    private static final String CHAPTER_URLS_SELECTOR = "div#novel_contents > div#novel_color > div.index_box > dl.novel_sublist2 > dd.subtitle > a";

    private static final String HREF_SELECTOR = "href";

    private static final String VOLUME_TITLES_SELECTOR = "#novel_contents > div#novel_color > div.index_box > div.chapter_title";

    private static final String CHAPTER_TITLE_SELECTOR = "#novel_contents > div#novel_color > div.novel_bn > p.novel_p";

    private static final String CHAPTER_TEXT_SELECTOR = "div#novel_contents > div#novel_color";

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

    public boolean hasVolumes(Document document) {
        try {
            document.selectFirst(VOLUME_TITLES_SELECTOR).hasText();
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public List<String> parseVolumeTitles(Document document) {
        Elements elements = document.select(VOLUME_TITLES_SELECTOR);
        List<String> titles = new ArrayList<>();
        elements.forEach(element -> {
            titles.add(element.text());
        });
        return titles;
    }

    public String parseChapterTitle(Document document) {
        return document.selectFirst(CHAPTER_TITLE_SELECTOR).text();
    }

    public String parseChapterText(Document document) {
        return document.selectFirst(CHAPTER_TEXT_SELECTOR).outerHtml();
    }

    public List<String> parseAllChapterUrls(Document document) {
        List<String> urls = new ArrayList<>();
        Elements elements = document.select(CHAPTER_URLS_SELECTOR);
        elements.forEach(element -> {
            urls.add(element.attr(HREF_SELECTOR));
        });
        return urls;
    }

    public Map<Integer, List<String>> sortChaptersByVolume(Document document) {
        // TODO finish implementation
        return null;
    }

    public Integer parseChapterNumber(Document document) {
        // TODO finish implementation
        return null;
    }

}
