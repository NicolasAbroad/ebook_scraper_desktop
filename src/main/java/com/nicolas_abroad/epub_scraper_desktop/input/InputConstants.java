package com.nicolas_abroad.epub_scraper_desktop.input;

/**
 * Input constants.
 *
 * @author Nicolas
 */
public class InputConstants {

	private InputConstants() {
	}

	private static final String SYSOSETSU_BASE_URL_REGEX = "^((https://)||(http://))?((ncode|novel18).syosetu.com/n)(\\d{4})([a-z]{1,2})";
	public static final String SYSOSETSU_URL_REGEX = SYSOSETSU_BASE_URL_REGEX + "(/)?$";
	public static final String SYSOSETSU_URL_MULTIPAGE_REGEX = SYSOSETSU_BASE_URL_REGEX + "(/)?\\?p=\\d+(/)?$";
	public static final String SYSOSETSU_URL_CHAPTER_REGEX = SYSOSETSU_BASE_URL_REGEX + "(/)?\\d+(/)?$";
	public static final String KAKUYOMU_URL_REGEX = "^((https://)||(http://))?(kakuyomu.jp/works/)(\\d{19,20})(/)?$";
}
