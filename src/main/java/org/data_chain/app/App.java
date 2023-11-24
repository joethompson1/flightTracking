package org.data_chain.app;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

import org.data_chain.adsbAircraft.AdsbAircraftFetcher;
import org.data_chain.adsbAircraft.Flight;

/**
 * Main application class for fetching and displaying aircraft information.
 */
public class App {

    /**
     * The main entry point of the application.
     * @param args The command-line arguments (not used in this application).
     */
    public static void main( String[] args ) {

        try {
            // Load environment variables
            EnvVariables env = new EnvVariables();
            final String apiKey = env.apiKey;
            final String baseUrl = env.baseUrl;
            final double latitude = env.latitude;
            final double longitude = env.longitude;
            final int interval = env.interval;
            final int radius = env.radius;

            // Initialise aircraftFetcher and fetch all aircraft within x Nautical Miles
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
