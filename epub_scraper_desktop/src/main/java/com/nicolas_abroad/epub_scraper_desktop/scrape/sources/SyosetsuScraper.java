package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

/**
 * Ebook scraper for syosetsu.com
 * @author Nicolas
 */
public class SyosetsuScraper implements EbookScraper {

    private static final String AUTHOR_SELECTOR_WITH_LINK = "div#novel_contents > div#novel_color > div.novel_writername > a";

    private static final String AUTHOR_SELECTOR_WITHOUT_LINK = "div#novel_contents > div#novel_color > div.novel_writername";

    private static final String STORY_TITLE_SELECTOR = "div#novel_contents > div#novel_color > p.novel_title";

    private static final String CHAPTER_URLS_SELECTOR = "div#novel_contents > div#novel_color > div.index_box > dl.novel_sublist2 > dd.subtitle > a";

    private static final String CHAPTER_URLS_CONTAINER = "div#novel_contents > div#novel_color > div.index_box";

    private static final String VOLUME_TITLE_SELECTOR_IN_CHAPTER_URL_CONTAINER = "div.chapter_title";

    private static final String CHAPTER_TITLE_CONTAINER_SELECTOR_IN_CHAPTER_URL_CONTAINER = "dl.novel_sublist2";

    private static final String CHAPTER_TITLE_SELECTOR_IN_CHAPTER_URL_CONTAINER = "dd.subtitle > a";

    private static final String HREF_SELECTOR = "href";

    private static final String VOLUME_TITLES_SELECTOR = "#novel_contents > div#novel_color > div.index_box > div.chapter_title";

    private static final String CHAPTER_TITLE_SELECTOR = "#novel_contents > div#novel_color > p.novel_subtitle";

    private static final String CHAPTER_TEXT_SELECTOR = "div#novel_contents > div#novel_color";

    private static final String CHAPTER_NUMBER = "div#novel_contents > div#novel_color > div#novel_no";

    private static final String SYSOSETSU_URL = "https://ncode.syosetu.com";

    public Document parseHTMLDocument(String url) throws IOException {
        Document document = Jsoup.connect(url).get().normalise();
        OutputSettings settings = new OutputSettings();
        settings.escapeMode(EscapeMode.xhtml);
        settings.prettyPrint(false);
        settings.indentAmount(0);
        settings.charset("UTF-8");
        document.outputSettings(settings);
        return document;
    }

    public String parseAuthor(Document document) {
        String author = null;
        try {
            author = document.selectFirst(AUTHOR_SELECTOR_WITH_LINK).text();
        } catch (NullPointerException e) {
            author = document.selectFirst(AUTHOR_SELECTOR_WITHOUT_LINK).text().substring(3);
        }
        return author;

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
        // Parse text
        StringBuilder text = new StringBuilder();
        Elements allElements = document.select(CHAPTER_TEXT_SELECTOR).first().children();
        for (Element element : allElements) {
            text.append(element.outerHtml().replaceAll("\u00a0", ""));
            // text.append(element.outerHtml());
        }

        // Clean text
        String chapterText = text.toString();
        chapterText = cleanChapterText(chapterText);
        return chapterText;
    }

    public List<String> parseAllChapterUrls(Document document) {
        List<String> urls = new ArrayList<>();
        Elements elements = document.select(CHAPTER_URLS_SELECTOR);
        elements.forEach(element -> {
            urls.add(SYSOSETSU_URL + element.attr(HREF_SELECTOR));
        });
        return urls;
    }

    public Map<Integer, List<String>> parseChapterUrlsByVolume(Document document) {
        Map<Integer, List<String>> volumes = new HashMap<Integer, List<String>>();
        Elements elements = document.selectFirst(CHAPTER_URLS_CONTAINER).children();
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).is(VOLUME_TITLE_SELECTOR_IN_CHAPTER_URL_CONTAINER)) {
                volumes.put(volumes.size() + 1, new ArrayList<String>());
            } else if (elements.get(i).is(CHAPTER_TITLE_CONTAINER_SELECTOR_IN_CHAPTER_URL_CONTAINER)) {
                volumes.get(volumes.size()).add(SYSOSETSU_URL + elements.get(i)
                        .selectFirst(CHAPTER_TITLE_SELECTOR_IN_CHAPTER_URL_CONTAINER).attr(HREF_SELECTOR));
            }
        }
        return volumes;
    }

    public int parseChapterNumber(Document document) {
        String number = document.selectFirst(CHAPTER_NUMBER).text().split("/")[0];
        return Integer.parseInt(number);
    }

    private String cleanChapterText(String text) {
        text = text.replaceAll("\u00a0", "");
        text = text.replaceAll("<br>", "<br></br>");
        text = text.replaceAll("<img>", "<img></img>");
        text = text.replaceAll("border[\\w\\W].*?\"[\\w\\W]*?\"", "");
        text = text.replaceAll("<img(.*?)>", "<img$1></img>");
        return text;
    }

}
