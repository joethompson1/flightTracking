package org.data_chain.app;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.data_chain.adsbAircraft.AdsbAircraftFetcher;
import org.data_chain.adsbAircraft.Flight;
import org.data_chain.adsbAircraft.Flights;
import org.data_chain.database.Postgres;
import org.data_chain.database.AdsbRequestData;

/**
 * Main application class for fetching and displaying aircraft information.
 */
public class App {
    private static EnvVariables env;
    private static Timer timer;
    private static Postgres postgres;

    /**
     * The main entry point of the application.
     * @param args The command-line arguments (not used in this application).
     */
    public static void main( String[] args ) {

        try {
            // Load environment variables
            env = new EnvVariables("config/config.properties");
            postgres = new Postgres(env.postgresURL, env.postgresUser, env.postgresPassword);

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    makeApiRequest();
                }
            }, 0, env.waitTime);


        } catch(RuntimeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Makes an API request to fetch aircraft data and updates the database.
     */
    private static void makeApiRequest() {

        // Get the current time
        Date currentTime = new Date();

        // Extract configuration values
        int startHour = env.startTime;
        int endHour = env.endTime;
        int maxNumberRequests = env.maxNumberRequests;

        // Retrieve AdsbRequestData from the database
        AdsbRequestData adsbRequestData = postgres.getAdsbRequestData(env.user);

        // Check if there was an issue retrieving AdsbRequestData
        if (adsbRequestData == null) {
            System.out.println("Error getting AdsbRequestData");
            return;
        }

        // Extract information from AdsbRequestData
        int numberOfRequests = adsbRequestData.requestCount;
        int resetDay = adsbRequestData.resetDate;
        boolean resetOccurred = adsbRequestData.resetOccurred;

        // Check if the numberOfRequests should be reset to 0
        if (currentTime.getDate() == resetDay && !resetOccurred) {
            System.out.println("Resetting number of requests");
            numberOfRequests = postgres.updateRequestCountToZero(env.user);
        }

        // The day after the numberOfRequests is reset, change resetOccurred to false
        if (currentTime.getDate() == resetDay + 1) {
            System.out.println("Changing DB to reset occurred false");
            postgres.updateResetOccurredToFalse(env.user);
        }

        // Check if monthly request limit has been reached
        if (numberOfRequests >= maxNumberRequests) {
            System.out.println("Exceeded monthly request limit");
            return;
        }

        // Checks if currentTime is within specified operating hours
        if (currentTime.getHours() <= startHour || currentTime.getHours() > endHour) {
            System.out.println("Not running outside of " + startHour + " - " + endHour + ".");
            return;
        }

        try {
            // Initialise aircraftFetcher and fetch all aircraft within x Nautical Miles
            AdsbAircraftFetcher aircraftFetcher = new AdsbAircraftFetcher(env.apiKey, env.baseUrl, env.latitude, env.longitude);
            Flights flights = aircraftFetcher.getAircraft_x_NM(env.radius);
            
            // Increments requestCount by 1
            postgres.updateRequestCountByOne(env.user);

            System.out.println("-------- Request: " + (++numberOfRequests) + " --------");
            System.out.println(flights.timeOfRequest);
            System.out.println("Found: " + flights.flightsArray.size() + " flights within " + env.radius + " NM");
            postgres.createFlights(flights.flightsArray, flights.timeOfRequest);
            System.out.println("Successfully inserted: " + flights.flightsArray.size() + " flights");

        } catch(Exception e) {
            System.err.println("Error fetching aircraft data: " + e.getMessage());
        }
    }
}
