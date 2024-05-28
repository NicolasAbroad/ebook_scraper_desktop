package com.nicolas_abroad.epub_scraper_desktop.user_interface;

import com.nicolas_abroad.epub_scraper_desktop.input.InputParser;
import com.nicolas_abroad.epub_scraper_desktop.scrape.ScrapeExecutor;

import java.util.Scanner;

/**
 * CLI for ebook scrapping app.
 *
 * @author Nicolas
 */
public class CommandLineInterface {

	private static final class MSG {
		private static final String WAITING_INPUT = "Please input a url to scrape.";
		private static final String SCRAPING = "Please wait a moment while the app is scraping.";
		private static final String INCORRECT_URL = "Please input a correct url.\n";
		private static final String ERROR = "An error occurred while scraping.\n";
		private static final String FINISHED = "The app has finished scraping.\n";
	}

	public static void main(String[] args) {
		executePromptInterface();
	}

	private static void executePromptInterface() {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println(MSG.WAITING_INPUT);
			while (scanner.hasNext()) {
				String input = scanner.nextLine();

				if ("exit".equalsIgnoreCase(input)) {
					break;
				}

				// Scrape inputted url
				scrape(input);
				System.out.println(MSG.WAITING_INPUT);
			}
		}
	}

	private static void scrape(String input) {
		// Validate input
		InputParser inputParser = InputParser.getInputParser();
		inputParser.processUrl(input);
		if (!inputParser.isValidUrl()) {
			// Inputted index url is incorrect
			System.out.println(MSG.INCORRECT_URL);
			return;
		}

		System.out.println(MSG.SCRAPING);

		boolean executedCorrectly = ScrapeExecutor.executeScraping(input);
		if (!executedCorrectly) {
			System.out.println(MSG.ERROR);
			return;
		}

		System.out.println(MSG.FINISHED);
	}

}
