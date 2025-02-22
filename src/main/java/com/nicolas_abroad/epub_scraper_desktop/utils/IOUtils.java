package com.nicolas_abroad.epub_scraper_desktop.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class IOUtils {

	private IOUtils() {
	}

	/** Calculate CRC-32 value */
	public static long calculateCrc(byte[] data) {
		CRC32 crc = new CRC32();
		crc.update(data);
		return crc.getValue();
	}

	public static File getResource(String name) {
		return getResource(name, "main");
	}

	public static File getResource(String name, String folder) {
		return new File("src/" + folder + "/resources/" + name);
	}

	public static String getFileContent(File file) throws IOException {
		String content;
		try (FileInputStream fileInputStream = new FileInputStream(file);
			 InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
			 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
			String currentLine = null;
			StringBuilder sb = new StringBuilder();
			while ((currentLine = bufferedReader.readLine()) != null) {
				sb.append(currentLine);
				sb.append(System.lineSeparator());
			}
			content = sb.toString();
		}
		return content;
	}

}
