package com.nicolas_abroad.epub_scraper_desktop.input;

/**
 * Class which checks user input.
 * @author Nicolas
 */
public class InputChecking {

    private InputChecking() {
    }

    // TODO create regex for sysosetsu
    private static String sysosetsuUrlRegex = "";
    private static String[] urlRegexes = { sysosetsuUrlRegex };

    /**
     * Checks if url matches any of the possible urls regular expressions.
     * @param url
     * @return true if non-matching
     */
    public static boolean isIncorrectUrl(String url) {
        if (url == null) {
            return true;
        }
        for (String regex : urlRegexes) {
            if (url.matches(regex) == false) {
                return true;
            }
        }
        return false;
    }
}
