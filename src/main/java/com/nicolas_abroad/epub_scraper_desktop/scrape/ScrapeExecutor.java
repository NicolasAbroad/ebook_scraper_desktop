package com.nicolas_abroad.epub_scraper_desktop.scrape;

import com.nicolas_abroad.epub_scraper_desktop.ebook.Story;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;
import com.nicolas_abroad.epub_scraper_desktop.format.EbookFormat;
import com.nicolas_abroad.epub_scraper_desktop.format.EpubFormat;
import com.nicolas_abroad.epub_scraper_desktop.input.InputParser;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;
import com.nicolas_abroad.epub_scraper_desktop.user_interface.MessageEnum;
import com.nicolas_abroad.epub_scraper_desktop.utils.IOUtils;

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
	 * @param url target url
	 * @return whether scrapping completed successfully
	 */
	public static boolean executeScraping(String url) {
		return executeScraping(url, null);
	}

	public static boolean executeScraping(String url, Set<Integer> targetVolumeNumbers) {
		try {
			// Get target volume basic info
			Story story = generateStory(url);
			story.parseVolumeInfo(targetVolumeNumbers);

			// Create output directory
			Path outputDirectory = IOUtils.createOutputDirectory(story.getVolumes().getFirst().getAuthor());

			// Scrape & generate by volume
			EbookFormat ebookFormat = new EpubFormat();
			for (Volume volume : story.getVolumes()) {
				// Skip scraping & generation if already exists
				if (IOUtils.exists(outputDirectory, volume.getTitle() + ebookFormat.getFileExtension())) {
					System.out.println(volume.getTitle() + ": " + MessageEnum.SKIP_GENERATION.getMessage());
					continue;
				}

				volume.generate();
				System.out.println(volume.getTitle());
				ebookFormat.generate(outputDirectory, volume);
			}
			story.assignNonScrapedChapterNumbers();

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
