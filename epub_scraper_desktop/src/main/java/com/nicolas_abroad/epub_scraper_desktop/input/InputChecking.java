package com.nicolas_abroad.epub_scraper_desktop.input;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class which checks user input.
 * @author Nicolas
 */
public class InputChecking {

    private InputChecking() {
    }

    private static final String SYSOSETSU_URL_REGEX = "^((https://)||(http://))?(ncode.syosetu.com/n)(\\d{4})([a-z]{1,2})(/)?$";

    private static final String[] URL_REGEXES = { SYSOSETSU_URL_REGEX };

    /**
     * Checks if url matches any of the possible urls regular expressions.
     * @param url
     * @return true if non-matching
     */
    public static boolean isIncorrectUrl(String url) {
        if (url == null || url.isEmpty()) {
            return true;
        }

        for (String regex : URL_REGEXES) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return false;
            }
        }
        return true;
    }
}
