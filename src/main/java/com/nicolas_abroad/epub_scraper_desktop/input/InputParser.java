package com.nicolas_abroad.epub_scraper_desktop.input;

import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.KakuyomuScraper;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.SyosetsuScraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User input parsing.
 * @author Nicolas
 */
public class InputParser {

    private static InputParser singleton = new InputParser();
    private static final String SYSOSETSU_URL_REGEX = "^((https://)||(http://))?((ncode|novel18).syosetu.com/n)(\\d{4})([a-z]{1,2})(/)?$";
    private static final String KAKUYOMU_URL_REGEX = "^((https://)||(http://))?(kakuyomu.jp/works/)(\\d{19,20})(/)?$";

    private UrlScraperEnum urlScraper;

    /** Url to scraper enumerator */
    public enum UrlScraperEnum {
        /** Syosetsu mapper */
        SYSOSETSU(SYSOSETSU_URL_REGEX, new SyosetsuScraper()),
        /** Kakuyomu mapper */
        KAKUYOMU(KAKUYOMU_URL_REGEX, new KakuyomuScraper());

        private String regex;
        private EbookScraper scraper;

        /**
         * Get url regex.
         * @return url regex
         */
        public String getRegex() {
            return this.regex;
        }

        /**
         * Get scraper.
         * @return scraper
         */
        public EbookScraper getEbookScraper() {
            return this.scraper;
        }

        UrlScraperEnum(String regex, EbookScraper scraper) {
            this.regex = regex;
            this.scraper = scraper;
        }
    }

    private InputParser() {
    }

    /**
     * Get input parser.
     * @return input parser
     */
    public static InputParser getInputParser() {
        return singleton;
    }

    /**
     * Process url.
     * @param url
     */
    public void processUrl(String url) {
        if (url == null || url.isEmpty()) {
            this.urlScraper = null;
            return;
        }

        for (UrlScraperEnum urlScraper : UrlScraperEnum.values()) {
            Pattern pattern = Pattern.compile(urlScraper.regex);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                this.urlScraper = urlScraper;
                return;
            }
        }
        this.urlScraper = null;
    }

    /**
     * Is inputted url valid.
     * @return true when valid
     */
    public boolean isValidUrl() {
        return this.urlScraper != null;
    }

    /**
     * Get ebook scraper.
     * @return ebook scraper
     */
    public EbookScraper getEbookScraper() {
        if (this.urlScraper == null) {
            return null;
        }
        return this.urlScraper.getEbookScraper();
    }

}
