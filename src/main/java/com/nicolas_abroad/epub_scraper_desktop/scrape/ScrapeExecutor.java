package com.nicolas_abroad.epub_scraper_desktop.scrape;

import com.nicolas_abroad.epub_scraper_desktop.ebook.Story;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;
import com.nicolas_abroad.epub_scraper_desktop.format.EbookFormat;
import com.nicolas_abroad.epub_scraper_desktop.format.EpubFormat;
import com.nicolas_abroad.epub_scraper_desktop.input.InputParser;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Scraping execution.
 *
 * @author Nicolas
 */
public class ScrapeExecutor {

	private ScrapeExecutor() {
	}

	/**
	 * Execute scraping.
	 *
	 * @param url
	 * @return whether scrapping completed successfully
	 */
	public static boolean executeScraping(String url) {
		return executeScraping(url, null);
	}

	public static boolean executeScraping(String url, Set<Integer> targetVolumeNumbers) {
		try {
			// Scrape all data from url
			Story story = generateStory(url);
			if (targetVolumeNumbers == null) {
				story.generateAllVolumes();
			} else {
				story.generateSpecifiedVolumes(targetVolumeNumbers);
			}

			// Generate volumes from scrapped data
			EbookFormat ebookFormat = new EpubFormat();
			for (Volume volume : story.getVolumes()) {
				System.out.println(volume.getTitle());
				ebookFormat.generate(volume);
			}

			return true;
		} catch (Exception e) {
			logExceptionToFile(e);
			return false;
		}
	}

	public static List<String> fetchAllVolumeInfo(String url) {
		try {
			Story story = generateStory(url);
			List<Volume> volumeList = story.parseAllVolumeInfo();
			return volumeList.stream().map(Volume::getTitle).toList();
		} catch (Exception e) {
			logExceptionToFile(e);
			return Collections.emptyList();
		}
	}

	private static Story generateStory(String url) {
		InputParser inputParser = InputParser.getInputParser();
		EbookScraper scraper = inputParser.getEbookScraper();
		return new Story(scraper, url);
	}

	private static void logExceptionToFile(Exception exception) {
		try {
			String pathString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_hhmmss")) + ".txt";
			Path path = Paths.get(pathString);

			StringWriter sw = new StringWriter();
			exception.printStackTrace(new PrintWriter(sw));
			String exceptionString = sw.toString();
			Files.write(path, exceptionString.getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
