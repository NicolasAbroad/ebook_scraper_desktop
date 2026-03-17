package com.nicolas_abroad.epub_scraper_desktop.user_interface;

import com.nicolas_abroad.epub_scraper_desktop.input.InputParser;
import com.nicolas_abroad.epub_scraper_desktop.input.TestConverter;
import com.nicolas_abroad.epub_scraper_desktop.scrape.ScrapeExecutor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * CLI for ebook scrapping app.
 *
 * @author Nicolas
 */
@Command(name = "Ebook scrapper app", description = "Description of ebook scraper app")
public class CommandLineInterface implements Callable<Integer> {

	@Option(names = {"-u", "--url"}, description = "Specify url of ebook to parse", required = true)
	private String targetUrl;

	@Option(names = {"-a", "--all"}, description = "Scrape all volumes")
	private boolean scrapeAllVolumes;

	@Option(names = {"-v", "--volumes"}, description = "Scrape specified volumes", converter = TestConverter.class)
	private Set<Integer> targetVolumeNumbers;

	@AllArgsConstructor
	@Getter
	private enum MSG {
		SCRAPING(-1, "Please wait a moment while the app is scraping."),
		FINISHED_INFO(0, "The app has finished scraping volume information." + System.lineSeparator()),
		FINISHED_SCRAPE(0, "The app has finished scraping target volumes." + System.lineSeparator()),
		ERROR(1, "An error occurred while scraping." + System.lineSeparator()),
		INCORRECT_PARAMETERS(2, "Please input correct parameters." + System.lineSeparator()),
		INCORRECT_URL(2, "Please input a correct url." + System.lineSeparator()),
		;
		private final int exitCode;
		private final String message;
	}

	public static void main(String[] args) {
		CommandLine commandLine = new CommandLine(new CommandLineInterface());
		int exitCode = commandLine.execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() {
		// Input validation
		boolean isValidInput = validateInput(targetUrl);
		if (!isValidInput) {
			return exitSystem(MSG.INCORRECT_URL);
		}

		// Specifying both all volumes and specific volumes makes no sense
		if (scrapeAllVolumes && targetVolumeNumbers != null) {
			return exitSystem(MSG.INCORRECT_PARAMETERS);
		}

		// When nothing specified, scrape volume information
		if (!scrapeAllVolumes && targetVolumeNumbers == null) {
			List<String> volumeTitleList = ScrapeExecutor.fetchAllVolumeInfo(targetUrl);
			volumeTitleList.forEach(System.out::println);
			return exitSystem(MSG.FINISHED_INFO);
		}

		System.out.println(MSG.SCRAPING.getMessage());

		// Scrape volumes based on user input
		boolean hasScraped = scrapeAllVolumes ? ScrapeExecutor.executeScraping(targetUrl) : ScrapeExecutor.executeScraping(targetUrl, targetVolumeNumbers);
		if (!hasScraped) {
			return exitSystem(MSG.ERROR);
		}

		return exitSystem(MSG.FINISHED_SCRAPE);
	}

	private boolean validateInput(String input) {
		InputParser inputParser = InputParser.getInputParser();
		inputParser.processUrl(input);
		return inputParser.isValidUrl();
	}

	private int exitSystem(MSG message) {
		System.out.println(message.getMessage());
		return message.getExitCode();
	}

}
