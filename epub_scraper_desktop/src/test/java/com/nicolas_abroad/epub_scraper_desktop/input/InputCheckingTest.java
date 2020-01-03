package com.nicolas_abroad.epub_scraper_desktop.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test class for the InputChecking class.
 * @author Nicolas
 */
public class InputCheckingTest {

    @Test
    public void testIsIncorrectUrl01() {
        String url = "https://ncode.syosetu.com/n4128bn/";
        boolean isIncorrect = InputChecking.isIncorrectUrl(url);
        assertFalse(isIncorrect);
    }

    @Test
    public void testIsIncorrectUrl02() {
        String url = "http://ncode.syosetu.com/n4128bn/";
        boolean isIncorrect = InputChecking.isIncorrectUrl(url);
        assertFalse(isIncorrect);
    }

    @Test
    public void testIsIncorrectUrl03() {
        String url = "ncode.syosetu.com/n4128bn/";
        boolean isIncorrect = InputChecking.isIncorrectUrl(url);
        assertFalse(isIncorrect);
    }

    @Test
    public void testIsIncorrectUrl04() {
        String url = "https://ncode.syosetu.com/nn128bn/";
        boolean isIncorrect = InputChecking.isIncorrectUrl(url);
        assertTrue(isIncorrect);
    }

    @Test
    public void testIsIncorrectUrl05() {
        String url = "ncode.syosetu.com/nn128bn/";
        boolean isIncorrect = InputChecking.isIncorrectUrl(url);
        assertTrue(isIncorrect);
    }

    @Test
    public void testIsIncorrectUrl06() {
        String url = "https://ncode.com/n4128bn/";
        boolean isIncorrect = InputChecking.isIncorrectUrl(url);
        assertTrue(isIncorrect);
    }

    @Test
    public void testIsIncorrectUrl07() {
        String url = "ncode.com/n4128bn/";
        boolean isIncorrect = InputChecking.isIncorrectUrl(url);
        assertTrue(isIncorrect);
    }

    @Test
    public void testIsIncorrectUrl08() {
        String url = "google.com/https://ncode.syosetu.com/n4128bn/";
        boolean isIncorrect = InputChecking.isIncorrectUrl(url);
        assertTrue(isIncorrect);
    }

    @Test
    public void testIsIncorrectUrl09() {
        String url = "google.com/ncode.syosetu.com/n4128bn/";
        boolean isIncorrect = InputChecking.isIncorrectUrl(url);
        assertTrue(isIncorrect);
    }

}
