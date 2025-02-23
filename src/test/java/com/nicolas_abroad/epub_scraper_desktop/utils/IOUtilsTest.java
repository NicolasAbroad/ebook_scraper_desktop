package com.nicolas_abroad.epub_scraper_desktop.utils;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

public class IOUtilsTest extends TestCase {

	public void testGetResourceFileName() {
		File file = IOUtils.getResource("IOUtils/test.txt");
		assertEquals("test.txt", file.getName());
	}

	public void testGetResourceFileText() throws IOException {
		File file = IOUtils.getResource("IOUtils/test.txt");
		String expected = "test12345";
		String actual = IOUtils.getFileContent(file);
		assertEquals(expected, actual);
	}

	public void testGetFileContent() throws IOException {
		String expected = "test12345";
		String actual = IOUtils.getFileContent("IOUtils/test.txt");
		assertEquals(expected, actual);
	}

}