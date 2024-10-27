package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.net.URLEncodedUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nicolas_abroad.epub_scraper_desktop.input.InputConstants.SYSOSETSU_URL_CHAPTER_REGEX;
import static com.nicolas_abroad.epub_scraper_desktop.input.InputConstants.SYSOSETSU_URL_MULTIPAGE_REGEX;

/**
 * Ebook scraper for syosetsu.com
 *
 * @author Nicolas
 */
@SuppressWarnings("deprecation")
public class SyosetsuScraper extends EbookScraper {

	private static final String BASE_URL = "https://ncode.syosetu.com";

	// Pagination parsing
	private static final String PAGINATION_BAR = ".c-pager";
	private static final String PAGINATION_LAST_PAGE = "a.c-pager__item--last";

	// Author parsing
	private static final String AUTHOR_SELECTOR = ".p-novel > .p-novel__author";

	// Title parsing
	private static final String STORY_TITLE_SELECTOR = ".p-novel > .p-novel__title";

	// Volume parsing
	private static final String CHAPTER_URLS_SELECTOR = ".p-novel > .p-eplist > .p-eplist__sublist > a";
	private static final String CHAPTER_URLS_CONTAINER = ".p-novel > .p-eplist";
	private static final String VOLUME_TITLE_SELECTOR_IN_CHAPTER_URL_CONTAINER = ".p-eplist__chapter-title";
	private static final String CHAPTER_TITLE_CONTAINER_SELECTOR_IN_CHAPTER_URL_CONTAINER = ".p-eplist__sublist";
	private static final String CHAPTER_TITLE_SELECTOR_IN_CHAPTER_URL_CONTAINER = "a.p-eplist__subtitle";
	private static final String VOLUME_TITLES_SELECTOR = ".p-novel > .p-eplist > .p-eplist__chapter-title";

	// Chapter parsing
	private static final String CHAPTER_TITLE_SELECTOR = ".p-novel .p-novel__title";
	private static final String CHAPTER_TEXT_SELECTOR = ".p-novel";
	private static final String CHAPTER_NUMBER = ".p-novel .p-novel__number";

	private String sessionId;

	// --------------------------------------
	// General Parsing
	// --------------------------------------

	public Document parseHTMLDocument(String url) throws Exception {
		Connection connection = Jsoup.connect(url).method(Method.GET);
		if (this.sessionId != null && !this.sessionId.isEmpty()) {
			set18PlusCookies(connection);
		}

		// Get html source
		Response response = connection.execute();
		Document document = response.parse().normalise();

		if (this.sessionId == null || this.sessionId.isEmpty()) {
			boolean is18Plus = is18Plus(document);
			if (is18Plus) {
				// Get session id when target url is from the 18+ section
				this.sessionId = response.cookie("ses");
				// Get html source again (with correct cookie data)
				connection = Jsoup.connect(url).method(Method.GET);
				set18PlusCookies(connection);
				response = connection.execute();
				document = response.parse().normalise();
			}
		}

		OutputSettings settings = new OutputSettings();
		settings.escapeMode(EscapeMode.xhtml);
		settings.prettyPrint(false);
		settings.indentAmount(0);
		settings.charset("UTF-8");
		document.outputSettings(settings);

		// Merge all pages if main index page
		if (!url.matches(SYSOSETSU_URL_MULTIPAGE_REGEX) && !url.matches(SYSOSETSU_URL_CHAPTER_REGEX)) {
			Integer lastPageNumber = getLastPageNumber(document);
			if (lastPageNumber != null) {
				scrapeAllIndexes(document, lastPageNumber);
			}
		}

		return document;
	}

	private boolean is18Plus(Document document) {
		try {
			document.select("h1:contains(年齢確認)").get(0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void set18PlusCookies(Connection connection) {
		connection.cookie("ses", this.sessionId);
		connection.cookie("over18", "yes");
	}

	// --------------------------------------
	// Volume Parsing
	// --------------------------------------

	public String parseAuthor(Document document) {
		String author = document.selectFirst(AUTHOR_SELECTOR).text();
		if (author.startsWith("作者：")) {
			author = author.substring(3);
		}
		return author;
	}

	public String parseStoryTitle(Document document) {
		return document.selectFirst(STORY_TITLE_SELECTOR).text();
	}

	public boolean hasVolumes(Document document) throws Exception {
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
		Map<Integer, List<String>> volumes = new HashMap<>();
		Elements elements = document.selectFirst(CHAPTER_URLS_CONTAINER).children();
		for (Element element : elements) {
			if (element.is(VOLUME_TITLE_SELECTOR_IN_CHAPTER_URL_CONTAINER)) {
				volumes.put(volumes.size() + 1, new ArrayList<>());
			} else if (element.is(CHAPTER_TITLE_CONTAINER_SELECTOR_IN_CHAPTER_URL_CONTAINER)) {
				Element chapterUrlElement = element.selectFirst(CHAPTER_TITLE_SELECTOR_IN_CHAPTER_URL_CONTAINER);
				String url = parseChapterUrl(chapterUrlElement);
				volumes.get(volumes.size()).add(url); // add to last volume
			}
		}
		return volumes;
	}

	private String parseChapterUrl(Element element) {
		return BASE_URL + element.attr("href");
	}

	private Integer getLastPageNumber(Document document) {
		Element paginationBar = document.selectFirst(PAGINATION_BAR);
		if (paginationBar == null) {
			return null;
		}
		String lastPageURL = paginationBar.selectFirst(PAGINATION_LAST_PAGE).attr("href");
		int paramsIndex = lastPageURL.indexOf("?") + 1;

		List<NameValuePair> params = URLEncodedUtils.parse(lastPageURL.substring(paramsIndex), StandardCharsets.UTF_8);

		String lastPageNumber = params.stream().filter(p -> "p".equals(p.getName())).findFirst().get().getValue();
		return Integer.parseInt(lastPageNumber);
	}

	private void scrapeAllIndexes(Document document, Integer lastPageNumber) throws Exception {
		for (int i = 2; i <= lastPageNumber; i++) {
			// Build url with page number as parameter
			URIBuilder uriBuilder = new URIBuilder(document.location());
			uriBuilder.addParameter("p", Integer.toString(i));

			// Scrape index entries
			Document page = parseHTMLDocument(uriBuilder.build().toString());
			Elements indexEntries = page.selectFirst(CHAPTER_URLS_CONTAINER).children();

			// Append index entries to original document
			Element indexElement = document.selectFirst(CHAPTER_URLS_CONTAINER);
			indexElement.insertChildren(-1, indexEntries);
		}
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
			// Remove advertising
			if (element.className().contains("ad") || element.className().contains("koukoku")) {
				continue;
			}

			text.append(element.outerHtml());
		}

		// Clean text
		String chapterText = text.toString();
		chapterText = cleanChapterText(chapterText);
		return chapterText;
	}

	public int parseChapterNumber(Document document) {
		String number = document.selectFirst(CHAPTER_NUMBER).text().split("/")[0];
		return Integer.parseInt(number);
	}

}
