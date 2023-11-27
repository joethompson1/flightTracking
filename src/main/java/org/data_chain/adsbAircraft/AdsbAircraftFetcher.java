package org.data_chain.adsbAircraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Fetches aircraft information from ADS-B Exchange API based on geographic coordinates.
 */
public class AdsbAircraftFetcher {

	private final String apiKey;
	private final String baseUrl;

	private final double latitude;
	private final double longitude;

	/**
     * Constructs an AdsbAircraftFetcher with the specified API key, base URL, latitude, and longitude.
     *
     * @param apiKey     The API key for accessing the ADS-B Exchange API.
     * @param baseUrl    The base URL of the ADS-B Exchange API.
     * @param latitude   The latitude of the geographic location.
     * @param longitude  The longitude of the geographic location.
     */
	public AdsbAircraftFetcher(String apiKey, String baseUrl, double latitude, double longitude) {
		this.apiKey = apiKey;
		this.baseUrl = baseUrl;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
     * Creates an HTTP connection to the specified API endpoint.
     *
     * @param endpoint 		The endpoint to be appended to the base URL.
     * @return 				A configured HttpURLConnection.
     * @throws IOException 	If an I/O exception occurs while creating the connection.
     */
	private HttpURLConnection createConnection(String endpoint) throws IOException {
		URL adsbUrl = new URL(this.baseUrl + endpoint);
		HttpURLConnection connection = (HttpURLConnection) adsbUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("X-RapidAPI-Key", this.apiKey);
		connection.setRequestProperty("X-RapidAPI-Host", "adsbexchange-com1.p.rapidapi.com");
		return connection;
	}

	/**
     * Retrieves a list of aircraft within a specified radius from the geographic coordinates.
     *
     * @param radius 	The radius within which to retrieve aircraft (in nautical miles).
     * @return 			Flights object containing list of flights and time of request.
     */
	public Flights getAircraft_x_NM(int radius) throws IOException {
		String endpoint = String.format("/lat/%s/lon/%s/dist/%d/", latitude, longitude, radius);

		try {
			HttpURLConnection connection = createConnection(endpoint);

			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();

				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				reader.close();

				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode adsbJsonResponse = objectMapper.readTree(response.toString());

				List<Flight> flightsArray = new ArrayList<>();
				for (JsonNode jsonFlight : adsbJsonResponse.get("ac")) {
					Flight flight = objectMapper.treeToValue(jsonFlight, Flight.class);
					flightsArray.add(flight);
				}

				Flights flights = new Flights(flightsArray, adsbJsonResponse.get("now").asLong());

				return flights;
			}

			else {
				throw new IOException("Failed to retrieve data. Response Code: " + responseCode);
			}

		} catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}