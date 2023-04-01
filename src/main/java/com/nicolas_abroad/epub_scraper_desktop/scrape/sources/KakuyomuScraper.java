package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

/**
 * Ebook scraper for kakuyomu.jp
 * @author Nicolas
 */
public class KakuyomuScraper extends EbookScraper {

    // Volume parsing
    private static final String BASE_URL = "https://kakuyomu.jp";
    private static final String AUTHOR_SELECTOR = "#workAuthor-activityName";
    private static final String STORY_TITLE_SELECTOR = "#workTitle";
    private static final String CHAPTER_URLS_SELECTOR = "li.widget-toc-episode > a";
    private static final String CHAPTER_URLS_CONTAINER = "ol.widget-toc-items";
    private static final String VOLUME_TITLE_CLASS = "widget-toc-level1";
    private static final String VOLUME_SUBTITLE_CLASS = "widget-toc-level2";
    private static final String VOLUME_TITLES_SELECTOR = "li.widget-toc-chapter.widget-toc-level1";

    // Chapter parsing
    private static final String CHAPTER_TITLE_SELECTOR = ".widget-episodeTitle";
    private static final String CHAPTER_TEXT_SELECTOR = "div#contentMain-inner";

    // --------------------------------------
    // General Parsing
    // --------------------------------------

    public Document parseHTMLDocument(String url) throws IOException {
        Connection connection = Jsoup.connect(url).method(Method.GET);

        // Get html source
        Response response = connection.execute();
        Document document = response.parse().normalise();

        OutputSettings settings = new OutputSettings();
        settings.escapeMode(EscapeMode.xhtml);
        settings.prettyPrint(false);
        settings.indentAmount(0);
        settings.charset("UTF-8");
        document.outputSettings(settings);
        return document;
    }

    // --------------------------------------
    // Volume Parsing
    // --------------------------------------

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
        for (Element element : elements) {
            titles.add(element.text());
        }
        return titles;
    }

    public List<String> parseAllChapterUrls(Document document) {
        List<String> urls = new ArrayList<>();
        Elements elements = document.select(CHAPTER_URLS_SELECTOR);
        for (Element element : elements) {
            String url = parseChapterUrl(element);
            urls.add(url);
        }
        return urls;
    }

    public Map<Integer, List<String>> parseChapterUrlsByVolume(Document document) {
        Map<Integer, List<String>> volumes = new HashMap<Integer, List<String>>();
        Elements elements = document.selectFirst(CHAPTER_URLS_CONTAINER).children();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            boolean isTitle = element.hasClass(VOLUME_TITLE_CLASS);
            boolean isSubtitle = element.hasClass(VOLUME_SUBTITLE_CLASS);
            if (i == 0 && !isTitle) {
                volumes.put(volumes.size() + 1, new ArrayList<String>());
            }
            if (isTitle) {
                volumes.put(volumes.size() + 1, new ArrayList<String>());
            } else if (isSubtitle) {
                // Skip if subtitle
            } else {
                Element urlElement = element.selectFirst("a");
                String url = parseChapterUrl(urlElement);
                volumes.get(volumes.size()).add(url); // add to last volume
            }
        }
        return volumes;
    }

    private String parseChapterUrl(Element element) {
        return BASE_URL + element.attr("href");
    }

    // --------------------------------------
    // Chapter Parsing
    // --------------------------------------

    public String parseChapterTitle(Document document) {
        String titleText = document.selectFirst(CHAPTER_TITLE_SELECTOR).text();
        titleText = cleanChapterTitle(titleText);
        return titleText;
    }

    public String parseChapterText(Document document) {
        // Parse text
        StringBuilder text = new StringBuilder();
        Elements allElements = document.select(CHAPTER_TEXT_SELECTOR).first().children();
        for (Element element : allElements) {
            text.append(element.outerHtml());
        }

        // Clean text
        String chapterText = text.toString();
        chapterText = cleanChapterText(chapterText);
        return chapterText;
    }

    public int parseChapterNumber(Document document) {
        // No chapter number is provided
        return -1;
    }

}
