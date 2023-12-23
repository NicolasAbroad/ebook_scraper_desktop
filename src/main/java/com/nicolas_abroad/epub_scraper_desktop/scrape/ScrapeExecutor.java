package com.nicolas_abroad.epub_scraper_desktop.scrape;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.nicolas_abroad.epub_scraper_desktop.ebook.Story;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;
import com.nicolas_abroad.epub_scraper_desktop.format.EbookFormat;
import com.nicolas_abroad.epub_scraper_desktop.format.EpubFormat;
import com.nicolas_abroad.epub_scraper_desktop.input.InputParser;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;

/**
 * Scraping execution.
 * @author Nicolas
 */
public class ScrapeExecutor {

    /**
     * Execute scraping.
     * @param url
     * @return whether scrapping completed successfully
     */
    public static boolean executeScraping(String url) {
        try {
            System.out.println(url);

            // Scrape all data from url
            InputParser inputParser = InputParser.getInputParser();
            EbookScraper scraper = inputParser.getEbookScraper();
            Story story = new Story(scraper, url);
            story.generate();

            // Generate volumes from scrapped data
            EbookFormat ebookFormat = new EpubFormat();
            for (Volume volume : story.getVolumes()) {
                System.out.println(volume.getTitle());
                ebookFormat.generate(volume);
            }

            return true;
        } catch (Exception e) {
            try {
                String pathString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_hhmmss")) + ".txt";
                Path path = Paths.get(pathString);

                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionString = sw.toString();

                Files.write(path, exceptionString.getBytes());
            } catch (Exception e1) {
            }

            return false;
        }
    }

}
