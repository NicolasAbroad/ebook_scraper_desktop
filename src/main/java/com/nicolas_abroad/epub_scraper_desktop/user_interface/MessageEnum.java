package com.nicolas_abroad.epub_scraper_desktop.user_interface;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageEnum {
    STARTUP(-1, "Application %s (v%s) started successfully" + System.lineSeparator()),
    SCRAPING(-1, "Please wait a moment while the app is scraping." + System.lineSeparator()),
    SKIP_GENERATION(-1, "Skipping generation as volume already exists"),
    FINISHED_INFO(0, System.lineSeparator() + "The app has finished scraping volume information."),
    FINISHED_SCRAPE(0, System.lineSeparator() + "The app has finished scraping target volumes."),
    ERROR(1, System.lineSeparator() + "An error occurred while scraping."),
    INCORRECT_PARAMETERS(2, "Please input correct parameters."),
    INCORRECT_URL(2, "Please input a correct url."),
    ;
    private final int exitCode;
    private final String message;
}
