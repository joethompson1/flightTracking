package com.data_chain.app;

import java.util.Properties;
import java.io.InputStream;

public class EnvVariables {
    public final String apiKey;
    public final String baseUrl;
    public final double latitude;
    public final double longitude;
    public final int interval;

    public EnvVariables() {
        Properties properties = new Properties();

        try (InputStream input = EnvVariables.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }

            // Load the properties file
            properties.load(input);

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.apiKey = properties.getProperty("API_KEY");
        this.baseUrl = properties.getProperty("BASE_URL");
        this.latitude = parseDoubleProperty(properties, "LATITUDE");
        this.longitude = parseDoubleProperty(properties, "LONGITUDE");
        this.interval = parseIntProperty(properties, "INTERVAL");
    }

    private double parseDoubleProperty(Properties properties, String propertyName) {
        String value = properties.getProperty(propertyName);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Error parsing double property: " + propertyName, e);
            }
        }
        throw new RuntimeException("Missing double property: " + propertyName);
    }

    private int parseIntProperty(Properties properties, String propertyName) {
        String value = properties.getProperty(propertyName);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Error parsing int property: " + propertyName, e);
            }
        }
        throw new RuntimeException("Missing int property: " + propertyName);
    }
}
