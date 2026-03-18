package com.nicolas_abroad.epub_scraper_desktop.configuration;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;

public class AppConfiguration {

    private static final AppConfiguration singleton = new AppConfiguration();
    private final PropertiesConfiguration config;

    private AppConfiguration() {
        try {
            Configurations configs = new Configurations();
            File configurationFile = new File("src/main/resources/application.properties");
            config = configs.properties(configurationFile);
        } catch (Exception e) {
            throw new RuntimeException("Configuration file not found");
        }
    }

    private PropertiesConfiguration getConfiguration() {
        return singleton.config;
    }

    private static String getProperty(String key) {
        return singleton.getConfiguration().getProperty(key).toString();
    }

    public static String getApplicationName() {
        return getProperty("project.name");
    }

    public static String getApplicationVersion() {
        return getProperty("project.version");
    }

}