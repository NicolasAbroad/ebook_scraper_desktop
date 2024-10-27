package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Ebook scraper interface.
 * Sources define which elements have to be scrapped.
 *
 * @author Nicolas
 */
public abstract class EbookScraper {

	/**
	 * Parse HTML document.
	 *
	 * @param url
	 * @return HTML document
	 * @throws IOException
	 */
	public abstract Document parseHTMLDocument(String url) throws Exception;

	/**
	 * Parse author.
	 *
	 * @param document
	 * @return author
	 * @throws Exception
	 */
	public abstract String parseAuthor(Document document) throws Exception;

	/**
	 * Parse story title.
	 *
	 * @param document
	 * @return title
	 * @throws Exception
	 */
	public abstract String parseStoryTitle(Document document) throws Exception;

	/**
	 * Checks if story has volumes.
	 *
	 * @param document
	 * @return true if has volumes
	 * @throws Exception
	 */
	public abstract boolean hasVolumes(Document document) throws Exception;

	/**
	 * Parse volume titles.
	 *
	 * @param document
	 * @return list of volume titles
	 * @throws Exception
	 */
	public abstract List<String> parseVolumeTitles(Document document) throws Exception;

	/**
	 * Parse chapter title.
	 *
	 * @param document
	 * @return title
	 */
	public abstract String parseChapterTitle(Document document);

	/**
	 * Parse chapter text.
	 *
	 * @param document
	 * @return text
	 */
	public abstract String parseChapterText(Document document);

	/**
	 * Parse chapter urls. Used if story has no volumes.
	 *
	 * @param document
	 * @return chapter urls
	 * @throws Exception
	 */
	public abstract List<String> parseAllChapterUrls(Document document) throws Exception;

	/**
	 * Parse chapters by volume. Used if story has volumes.
	 *
	 * @param document
	 * @return chapters sorted by volume
	 * @throws Exception
	 */
	public abstract Map<Integer, List<String>> parseChapterUrlsByVolume(Document document) throws Exception;

	/**
	 * Parse chapter number.
	 *
	 * @param document
	 * @return chapter number
	 */
	public abstract int parseChapterNumber(Document document);

	static String cleanChapterTitle(String title) {
		title = title.replaceAll("&", "&amp;");
		title = title.replaceAll("<", "&lt;");
		title = title.replaceAll(">", "&gt;");
		return title;
	}

	static String cleanChapterText(String text) {
		text = text.replaceAll("\u00a0", "");
		text = text.replaceAll("<br>", "<br></br>");
		text = text.replaceAll("border[\\w\\W].*?\"[\\w\\W]*?\"", "");
		// Remove img tags
		text = text.replaceAll("<img(.*?)>", "");
		// Clear a tag attributes
		text = text.replaceAll(" href=\".*?\"", "");
		text = text.replaceAll(" target=\"_blank\"", "");
		return text;
	}

}
