package com.data_chain.app;

import java.util.Properties;
import java.io.InputStream;

public class EnvVariables {
    public final String apiKey;
    public final String baseUrl;
    public final double latitude;
    public final double longitude;
    public final int interval;
    public final int radius;

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

        this.apiKey = System.getenv("API_KEY") != null
            ? System.getenv("API_KEY") 
            : properties.getProperty("API_KEY");

        this.baseUrl = System.getenv("BASE_URL") != null
            ? System.getenv("BASE_URL") 
            : properties.getProperty("BASE_URL");

        this.latitude = System.getenv("LATITUDE") != null
            ? parseDoubleProperty(System.getenv("LATITUDE"))
            : parseDoubleProperty(properties.getProperty("LATITUDE"));

        this.longitude = System.getenv("LONGITUDE") != null
            ? parseDoubleProperty(System.getenv("LONGITUDE"))
            : parseDoubleProperty(properties.getProperty("LONGITUDE"));

        this.interval = System.getenv("INTERVAL") != null
            ? parseIntProperty(System.getenv("INTERVAL"))
            : parseIntProperty(properties.getProperty("INTERVAL"));

        this.radius = System.getenv("RADIUS") != null
            ? parseIntProperty(System.getenv("RADIUS"))
            : parseIntProperty(properties.getProperty("RADIUS"));
    }

    private double parseDoubleProperty(String value) {
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Error parsing double property: " + value, e);
            }
        }
        throw new RuntimeException("Missing double property: " + value);
    }

    private int parseIntProperty(String value) {
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Error parsing int property: " + value, e);
            }
        }
        throw new RuntimeException("Missing int property: " + value);
    }
}
