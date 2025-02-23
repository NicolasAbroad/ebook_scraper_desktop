package com.nicolas_abroad.epub_scraper_desktop.ebook;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;

/**
 * Class for a single chapter of a volume.
 *
 * @author Nicolas
 */
@Data
@NoArgsConstructor
public class Chapter {

	/** Scraper */
	private EbookScraper scraper;

	/** Chapter url */
	private String url;

	/** Chapter number */
	private int chapterNumber;

	/** Title */
	private String title;

	/** Text */
	private String text;

	/** Create a new chapter with a scraper and a url */
	public Chapter(EbookScraper scraper, String url) {
		this.scraper = scraper;
		this.url = url;
	}

	/** Scrape all relevant information for this chapter */
	public void generate() throws Exception {
		Document document = scraper.parseHTMLDocument(url);
		this.title = scraper.parseChapterTitle(document);
		this.text = scraper.parseChapterText(document);
		this.chapterNumber = scraper.parseChapterNumber(document);
	}

}
