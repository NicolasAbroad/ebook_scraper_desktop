package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Ebook scraper for kakuyomu.jp
 * @author Nicolas
 */
public class KakuyomuScraper extends EbookScraper {

    // Volume parsing
    private static final String BASE_URL = "https://kakuyomu.jp";

    // Chapter parsing
    private static final String CHAPTER_TITLE_SELECTOR = ".widget-episodeTitle";
    private static final String CHAPTER_TEXT_SELECTOR = "div#contentMain-inner";

    // Json parsing
    private static final String JSON_SELECTOR = "#__NEXT_DATA__";

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

    public String parseAuthor(Document document) throws Exception {
        JsonNode rootNode = parseJsonNode(document);
        String workId = parseWordId(rootNode);
        String authorReference = parseAuthorReference(rootNode, workId);
        JsonNode dataNode = findDataNode(rootNode);
        return dataNode.path(authorReference).path("activityName").asText();
    }

    public String parseStoryTitle(Document document) throws Exception {
        JsonNode rootNode = parseJsonNode(document);
        String workId = parseWordId(rootNode);
        return findWorkNode(rootNode, workId).path("title").asText();
    }

    public boolean hasVolumes(Document document) throws Exception {
        JsonNode rootNode = parseJsonNode(document);
        String workId = parseWordId(rootNode);
        JsonNode tocNode = findWorkNode(rootNode, workId).path("tableOfContents");
        return tocNode.size() > 1; // a story without volumes has one empty node
    }

    public List<String> parseVolumeTitles(Document document) throws Exception {
        List<String> titles = new ArrayList<>();
        JsonNode rootNode = parseJsonNode(document);
        String workId = parseWordId(rootNode);

        List<JsonNode> volumeNodes = findVolumeNodes(rootNode, workId);
        if (volumeNodes.size() == 1) {
            return titles; // a story without volumes has one empty node
        }

        JsonNode dataNode = findDataNode(rootNode);
        for (int i = 0; i < volumeNodes.size(); i++) {
            JsonNode volumeNode = volumeNodes.get(i);
            String volumeTitle = parseVolumeTitle(dataNode, volumeNode);
            if (volumeTitle == null) {
                continue;
            }
            titles.add(volumeTitle);
        }
        return titles;
    }

    public List<String> parseAllChapterUrls(Document document) throws Exception {
        JsonNode rootNode = parseJsonNode(document);
        String workId = parseWordId(rootNode);

        List<JsonNode> volumeNodes = findVolumeNodes(rootNode, workId);
        assert volumeNodes.size() == 1;
        JsonNode volumeNode = volumeNodes.get(0);

        return parseVolumeUrls(rootNode, workId, volumeNode);
    }

    public Map<Integer, List<String>> parseChapterUrlsByVolume(Document document) throws Exception {
        Map<Integer, List<String>> volumes = new HashMap<Integer, List<String>>();
        List<String> urls = null;
        List<String> subtitleUrls = null;

        JsonNode rootNode = parseJsonNode(document);
        String workId = parseWordId(rootNode);
        JsonNode dataNode = findDataNode(rootNode);

        List<JsonNode> volumeNodes = findVolumeNodes(rootNode, workId);
        for (int i = 0; i < volumeNodes.size(); i++) {
            JsonNode volumeNode = volumeNodes.get(i);

            boolean isVolumeTitleNode = isVolumeTitleNode(dataNode, volumeNode);

            urls = parseVolumeUrls(rootNode, workId, volumeNode);
            boolean hasVolumeUrls = !urls.isEmpty();

            if (isVolumeTitleNode && hasVolumeUrls) {
                volumes.put(volumes.size() + 1, urls);
            }

            if (isVolumeTitleNode && !hasVolumeUrls) {
                subtitleUrls = new ArrayList<>();
                volumes.put(volumes.size() + 1, subtitleUrls);
            }

            if (!isVolumeTitleNode && hasVolumeUrls) {
                subtitleUrls.addAll(urls);
            }

        }
        return volumes;
    }

    private JsonNode parseJsonNode(Document document) throws JsonMappingException, JsonProcessingException {
        ObjectMapper jsonMapper = new ObjectMapper();
        String json = document.selectFirst(JSON_SELECTOR).html();
        JsonNode rootNode = jsonMapper.readTree(json);
        return rootNode;
    }

    private String parseWordId(JsonNode rootNode) {
        return rootNode.path("query").path("workId").asText();
    }

    private JsonNode findDataNode(JsonNode rootNode) {
        return rootNode.path("props").path("pageProps").path("__APOLLO_STATE__");
    }

    private JsonNode findWorkNode(JsonNode rootNode, String workId) {
        return findDataNode(rootNode).path("Work:" + workId);
    }

    private String parseAuthorReference(JsonNode rootNode, String workId) {
        return findWorkNode(rootNode, workId).path("author").path("__ref").asText();
    }

    private List<JsonNode> findVolumeNodes(JsonNode rootNode, String workId) {
        List<JsonNode> volumeNodes = new ArrayList<>();
        JsonNode tocNode = findWorkNode(rootNode, workId).path("tableOfContents");
        JsonNode dataNode = findDataNode(rootNode);
        for (Iterator<JsonNode> iterator = tocNode.elements(); iterator.hasNext();) {
            JsonNode node = iterator.next();
            String volumeReference = node.path("__ref").asText();
            JsonNode volumeNode = dataNode.path(volumeReference);
            volumeNodes.add(volumeNode);
        }
        return volumeNodes;
    }

    private List<String> parseVolumeUrls(JsonNode rootNode, String workId, JsonNode volumeNode) {
        List<String> urls = new ArrayList<>();
        JsonNode chaptersNode = volumeNode.path("episodeUnions");
        JsonNode dataNode = findDataNode(rootNode);
        for (Iterator<JsonNode> iterator = chaptersNode.elements(); iterator.hasNext();) {
            JsonNode node = iterator.next();
            String chapterReference = node.path("__ref").asText();
            String chapterId = dataNode.path(chapterReference).path("id").asText();
            String url = generateChapterUrl(workId, chapterId);
            urls.add(url);
        }
        return urls;
    }

    private String parseVolumeTitle(JsonNode dataNode, JsonNode volumeNode) {
        JsonNode volumeTitleNode = findVolumeTitleNode(dataNode, volumeNode);
        if (isVolumeTitleNode(dataNode, volumeNode)) {
            String volumeTitle = volumeTitleNode.path("title").asText();
            return volumeTitle;
        }
        return null;
    }

    private JsonNode findVolumeTitleNode(JsonNode dataNode, JsonNode volumeNode) {
        String volumeTitleReference = volumeNode.path("chapter").path("__ref").asText();
        JsonNode volumeTitleNode = dataNode.path(volumeTitleReference);
        return volumeTitleNode;
    }

    private boolean isVolumeTitleNode(JsonNode dataNode, JsonNode volumeNode) {
        JsonNode volumeTitleNode = findVolumeTitleNode(dataNode, volumeNode);
        int level = volumeTitleNode.path("level").asInt();
        switch (level) {
        case 1: // title
            return true;
        case 2: // subtitle
        default:
            return false;
        }
    }

    private String generateChapterUrl(String workId, String chapterId) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("/works/");
        sb.append(workId);
        sb.append("/episodes/");
        sb.append(chapterId);
        return sb.toString();
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
