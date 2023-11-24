package com.data_chain.app;

import com.fasterxml.jackson.databind.JsonNode;

import com.data_chain.adsbAircraftFetcher.AdsbAircraftFetcher;

/**
 * 
 *
 */
public class App {

    public static void main( String[] args ) {

        try {
            EnvVariables env = new EnvVariables();
            final String apiKey = env.apiKey;
            final String baseUrl = env.baseUrl;
            final double latitude = env.latitude;
            final double longitude = env.longitude;
            final int interval = env.interval;

            AdsbAircraftFetcher aircraftFetcher = new AdsbAircraftFetcher(apiKey, baseUrl, latitude, longitude);

            JsonNode flightsObject = aircraftFetcher.getAircraft1NM();
            System.out.println(flightsObject);

        } catch(RuntimeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
