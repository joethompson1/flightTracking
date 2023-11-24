package com.data_chain.app;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

import com.data_chain.adsbAircraft.AdsbAircraftFetcher;
import com.data_chain.adsbAircraft.Flight;

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
            final int radius = env.radius;

            AdsbAircraftFetcher aircraftFetcher = new AdsbAircraftFetcher(apiKey, baseUrl, latitude, longitude);
            List<Flight> flights = aircraftFetcher.getAircraft_x_NM(radius);
            
            for (Flight flight : flights) {
                System.out.println(flight);
            }

        } catch(RuntimeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
