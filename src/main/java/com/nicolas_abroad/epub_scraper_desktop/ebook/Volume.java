package com.nicolas_abroad.epub_scraper_desktop.ebook;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for a single volume.
 *
 * @author Nicolas
 */
@Data
@NoArgsConstructor
public class Volume {

	/** Scraper */
	private EbookScraper scraper;
	/** Chapter urls */
	private List<String> chapterUrls;
	/** Chapters */
	private List<Chapter> chapters = new ArrayList<>();
	/** Author */
	private String author;
	/** Title */
	private String title;
	/** Formatted volume number */
	private String volumeNumber;

	/** Create a new volume with a scraper and a list of chapter urls */
	public Volume(EbookScraper scraper, List<String> chapterUrls) {
		this.scraper = scraper;
		this.chapterUrls = chapterUrls;
	}

	/** Scrape all chapters in the chapter urls list */
	public void generate() throws Exception {
		for (String url : chapterUrls) {
			Chapter chapter = new Chapter(scraper, url);
			chapter.generate();
			this.chapters.add(chapter);
		}
	}

}
