package com.nicolas_abroad.epub_scraper_desktop.input;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.SyosetsuScraper;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit test class for the InputParser class.
 *
 * @author Nicolas
 */
public class InputCheckingTest {

	/** Test valid syosetsu urls. */
	@Test
	public void testSyosetsuValidUrls() {
		String[] urls = {"https://ncode.syosetu.com/n4128bn/", "http://ncode.syosetu.com/n4128bn/",
				"ncode.syosetu.com/n4128bn/"};
		for (String url : urls) {
			InputParser inputParser = InputParser.getInputParser();
			inputParser.processUrl(url);

			boolean isValid = inputParser.isValidUrl();
			assertTrue(isValid);

			SyosetsuScraper scraper = (SyosetsuScraper) inputParser.getEbookScraper();
			assertNotNull(scraper);
		}
	}

	/** Test invalid syosetsu urls. */
	@Test
	public void testSyosetsuInvalidUrls() {
		String[] urls = {"https://ncode.syosetu.com/nn128bn/", "ncode.syosetu.com/nn128bn/",
				"https://ncode.com/n4128bn/", "ncode.com/n4128bn/", "google.com/https://ncode.syosetu.com/n4128bn/",
				"google.com/ncode.syosetu.com/n4128bn/"};
		for (String url : urls) {
			InputParser inputParser = InputParser.getInputParser();
			inputParser.processUrl(url);

			boolean isValid = inputParser.isValidUrl();
			assertFalse(isValid);

			EbookScraper scraper = inputParser.getEbookScraper();
			assertNull(scraper);
		}
	}

	/** Test valid kakuyomu urls. */
	@Test
	public void testKakuyomuValidUrls() {
		String[] urls = {"https://kakuyomu.jp/works/4852201425154898215",
				"http://kakuyomu.jp/works/4852201425154898215", "kakuyomu.jp/works/4852201425154898215"};
		for (String url : urls) {
			InputParser inputParser = InputParser.getInputParser();
			inputParser.processUrl(url);

			boolean isValid = inputParser.isValidUrl();
			assertTrue(isValid);
		}
	}

	/** Test invalid kakuyomu urls. */
	@Test
	public void testKakuyomuInvalidUrls() {
		String[] urls = {"https://kakuyomu.jp/works/XXX2201425154898215", "kakuyomu.jp/works/XXX2201425154898215",
				"https://kakuyomu.jp/4852201425154898215", "kakuyomu.jp/4852201425154898215",
				"google.com/https://kakuyomu.jp/works/4852201425154898215",
				"google.com/kakuyomu.jp/works/4852201425154898215"};
		for (String url : urls) {
			InputParser inputParser = InputParser.getInputParser();
			inputParser.processUrl(url);

			boolean isValid = inputParser.isValidUrl();
			assertFalse(isValid);
		}
	}

}
