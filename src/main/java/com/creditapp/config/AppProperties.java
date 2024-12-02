package com.creditapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class manages application properties loaded from the "application.properties" file.
 * It implements the Singleton pattern to ensure only one instance of AppProperties exists.
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private static AppProperties instance;
    private Properties properties;

    /**
     * Private constructor to initialize properties and load them from the file.
     */
    private AppProperties() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Provides the singleton instance of AppProperties.
     *
     * @return The single instance of AppProperties.
     */
    public static synchronized AppProperties getInstance() {
        if (instance == null) {
            instance = new AppProperties();
        }
        return instance;
    }

    /**
     * Loads the properties from the "application.properties" file.
     * If the file cannot be found or loaded, an error message is logged.
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Unable to find application.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error loading application properties: " + ex.getMessage());
        }
    }

    /**
     * Retrieves a property value as a String.
     *
     * @param key The property key.
     * @return The property value as a String.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Retrieves a property value as a String with a default value if the key is not found.
     *
     * @param key The property key.
     * @param defaultValue The default value if the key is not found.
     * @return The property value or the default value if not found.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Retrieves a property value as an Integer.
     * If the value is not a valid Integer, logs an error and returns null.
     *
     * @param key The property key.
     * @return The property value as an Integer or null if invalid.
     */
    public Integer getIntProperty(String key) {
        String value = properties.getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer value for key: " + key);
            return null;
        }
    }

    /**
     * Retrieves a property value as an Integer with a default value if the key is not found or invalid.
     *
     * @param key The property key.
     * @param defaultValue The default value if the key is not found or invalid.
     * @return The property value as an Integer or the default value if not found or invalid.
     */
    public int getIntProperty(String key, int defaultValue) {
        Integer value = getIntProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Retrieves a property value as a Boolean.
     *
     * @param key The property key.
     * @return The property value as a Boolean or null if invalid.
     */
    public Boolean getBooleanProperty(String key) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : null;
    }

    /**
     * Retrieves a property value as a Boolean with a default value if the key is not found.
     *
     * @param key The property key.
     * @param defaultValue The default value if the key is not found.
     * @return The property value as a Boolean or the default value if not found.
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        Boolean value = getBooleanProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Reloads the properties from the "application.properties" file.
     */
    public void reloadProperties() {
        loadProperties();
    }
}
