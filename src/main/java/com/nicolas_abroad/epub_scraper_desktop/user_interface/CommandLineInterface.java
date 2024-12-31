package com.nicolas_abroad.epub_scraper_desktop.user_interface;

import com.nicolas_abroad.epub_scraper_desktop.input.InputParser;
import com.nicolas_abroad.epub_scraper_desktop.input.TestConverter;
import com.nicolas_abroad.epub_scraper_desktop.scrape.ScrapeExecutor;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;
import java.util.Set;

/**
 * CLI for ebook scrapping app.
 *
 * @author Nicolas
 */
@Command(name = "Ebook scrapper app", description = "Description of ebook scraper app")
public class CommandLineInterface implements Runnable {

	@Option(names = {"-u", "--url"}, description = "Specify url of ebook to parse", required = true)
	private String targetUrl;

	@Option(names = {"-a", "--all"}, description = "Scrape all volumes")
	private boolean scrapeAllVolumes;

	@Option(names = {"-v", "--volumes"}, description = "Scrape specified volumes", converter = TestConverter.class)
	private Set<Integer> targetVolumeNumbers;

	private static final class MSG {
		private static final String INCORRECT_PARAMETERS = "Please input correct parameters.";
		private static final String WAITING_INPUT = "Please input a url to scrape.";
		private static final String SCRAPING = "Please wait a moment while the app is scraping.";
		private static final String INCORRECT_URL = "Please input a correct url.\n";
		private static final String ERROR = "An error occurred while scraping.\n";
		private static final String FINISHED = "The app has finished scraping.\n";
	}

	public static void main(String[] args) {
		CommandLine commandLine = new CommandLine(new CommandLineInterface());
		commandLine.execute(args);
	}

	@Override
	public void run() {
		boolean isValidInput = validateInput(targetUrl);
		if (!isValidInput) {
			System.out.println(MSG.INCORRECT_URL);
		}

		if (scrapeAllVolumes && targetVolumeNumbers != null) {
			System.out.println(MSG.INCORRECT_PARAMETERS);
		}

		if (!scrapeAllVolumes && targetVolumeNumbers == null) {
			List<String> volumeTitleList = ScrapeExecutor.fetchAllVolumeInfo(targetUrl);
			volumeTitleList.forEach(System.out::println);
			return;
		}

		System.out.println(MSG.SCRAPING);

		boolean hasScraped = scrapeAllVolumes ? ScrapeExecutor.executeScraping(targetUrl) : ScrapeExecutor.executeScraping(targetUrl, targetVolumeNumbers);
		if (!hasScraped) {
			System.out.println(MSG.ERROR);
			return;
		}

		System.out.println(MSG.FINISHED);
	}

	private boolean validateInput(String input) {
		InputParser inputParser = InputParser.getInputParser();
		inputParser.processUrl(input);
		return inputParser.isValidUrl();
	}

}
