package com.nicolas_abroad.epub_scraper_desktop.user_interface;

import com.nicolas_abroad.epub_scraper_desktop.configuration.AppConfiguration;
import com.nicolas_abroad.epub_scraper_desktop.input.InputParser;
import com.nicolas_abroad.epub_scraper_desktop.input.VolumeInputConverter;
import com.nicolas_abroad.epub_scraper_desktop.scrape.ScrapeExecutor;
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

	@Option(names = {"-v", "--volumes"}, description = "Scrape specified volumes. Use comma-separated values (1,2,4) or ranges (2-5).", converter = VolumeInputConverter.class)
	private Set<Integer> targetVolumeNumbers;

	public static void main(String[] args) {
		CommandLine commandLine = new CommandLine(new CommandLineInterface());
		int exitCode = commandLine.execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() {
		outputCurrentVersion();

		// Input validation
		boolean isValidInput = validateInput(targetUrl);
		if (!isValidInput) {
			return exitSystem(MessageEnum.INCORRECT_URL);
		}

		// Specifying both all volumes and specific volumes makes no sense
		if (scrapeAllVolumes && targetVolumeNumbers != null) {
			return exitSystem(MessageEnum.INCORRECT_PARAMETERS);
		}

		// When nothing specified, scrape volume information
		if (!scrapeAllVolumes && targetVolumeNumbers == null) {
			List<String> volumeTitleList = ScrapeExecutor.fetchAllVolumeInfo(targetUrl);
			volumeTitleList.forEach(System.out::println);
			return exitSystem(MessageEnum.FINISHED_INFO);
		}

		System.out.println(MessageEnum.SCRAPING.getMessage());

		// Scrape volumes based on user input
		boolean hasScraped = scrapeAllVolumes ? ScrapeExecutor.executeScraping(targetUrl) : ScrapeExecutor.executeScraping(targetUrl, targetVolumeNumbers);
		if (!hasScraped) {
			return exitSystem(MessageEnum.ERROR);
		}

		return exitSystem(MessageEnum.FINISHED_SCRAPE);
	}

	private boolean validateInput(String input) {
		InputParser inputParser = InputParser.getInputParser();
		inputParser.processUrl(input);
		return inputParser.isValidUrl();
	}

	private int exitSystem(MessageEnum message) {
		System.out.println(message.getMessage());
		return message.getExitCode();
	}

	private void outputCurrentVersion() {
		String name = AppConfiguration.getApplicationName();
		String version = AppConfiguration.getApplicationVersion();
		System.out.printf((MessageEnum.STARTUP.getMessage()), name, version);
	}

}
