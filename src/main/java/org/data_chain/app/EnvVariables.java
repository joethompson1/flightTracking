package org.data_chain.app;

import java.util.Properties;
import java.io.InputStream;

public class EnvVariables {
    public final String user;
    public final String apiKey;
    public final String baseUrl;
    public final double latitude;
    public final double longitude;
    public final int waitTime;
    public final int radius;
    public final int startTime;
    public final int endTime;
    public final int maxNumberRequests;

    // Postgres env variables
    public final String postgresURL;
    public final String postgresUser;
    public final String postgresPassword;

    public EnvVariables(String configFilePath) {
        Properties properties = new Properties();

        try (InputStream input = EnvVariables.class.getClassLoader().getResourceAsStream(configFilePath)) {
            if (input != null) {
                // Load the properties file
                properties.load(input);
            } else {
                // Handles the case when configFilePath is not found
                System.err.println("Config file not found: " + configFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.user = System.getenv("APPLICATION_USER") != null
            ? System.getenv("APPLICATION_USER") 
            : properties.getProperty("APPLICATION_USER");

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

        this.waitTime = System.getenv("WAIT_TIME") != null
            ? parseIntProperty(System.getenv("WAIT_TIME"))
            : parseIntProperty(properties.getProperty("WAIT_TIME"));

        this.radius = System.getenv("RADIUS") != null
            ? parseIntProperty(System.getenv("RADIUS"))
            : parseIntProperty(properties.getProperty("RADIUS"));

        this.startTime = System.getenv("START_TIME") != null
            ? parseIntProperty(System.getenv("START_TIME"))
            : parseIntProperty(properties.getProperty("START_TIME"));

        this.endTime = System.getenv("END_TIME") != null
            ? parseIntProperty(System.getenv("END_TIME"))
            : parseIntProperty(properties.getProperty("END_TIME"));

        this.maxNumberRequests = System.getenv("MAX_NUMBER_REQUESTS") != null
            ? parseIntProperty(System.getenv("MAX_NUMBER_REQUESTS"))
            : parseIntProperty(properties.getProperty("MAX_NUMBER_REQUESTS"));

        this.postgresURL = System.getenv("POSTGRES_URL") != null
            ? System.getenv("POSTGRES_URL") 
            : properties.getProperty("POSTGRES_URL");

        this.postgresUser = System.getenv("POSTGRES_USER") != null
            ? System.getenv("POSTGRES_USER") 
            : properties.getProperty("POSTGRES_USER");

        this.postgresPassword = System.getenv("POSTGRES_PASSWORD") != null
            ? System.getenv("POSTGRES_PASSWORD") 
            : properties.getProperty("POSTGRES_PASSWORD");
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
