package com.nicolas_abroad.epub_scraper_desktop.ebook;

import com.nicolas_abroad.epub_scraper_desktop.format.EbookFormat;

import java.util.List;

/**
 * Interface for Ebooks
 *
 * @author Nicolas
 */
public interface Ebook {

	/**
	 * Set ebook's format.
	 *
	 * @param ebookFormat
	 */
	void setEbookFormat(EbookFormat ebookFormat);

	/**
	 * Generate all ebooks.
	 *
	 * @param url
	 * @return list containing all ebooks
	 */
	List<Volume> generateEbooks(String url);

}
