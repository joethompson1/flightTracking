package com.data_chain.adsbAircraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class AdsbAircraftFetcher {

	private final String apiKey;
	private final String baseUrl;

	private final double latitude;
	private final double longitude;

	public AdsbAircraftFetcher(String apiKey, String baseUrl, double latitude, double longitude) {
		this.apiKey = apiKey;
		this.baseUrl = baseUrl;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private HttpURLConnection createConnection(String endpoint) throws IOException {
		URL adsbUrl = new URL(this.baseUrl + endpoint);
		HttpURLConnection connection = (HttpURLConnection) adsbUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("X-RapidAPI-Key", this.apiKey);
		connection.setRequestProperty("X-RapidAPI-Host", "adsbexchange-com1.p.rapidapi.com");
		return connection;
	}

	public List<Flight> getAircraft_x_NM(int radius) {
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

				System.out.println(adsbJsonResponse);

				List<Flight> flights = new ArrayList<>();
				for (JsonNode jsonFlight : adsbJsonResponse.get("ac")) {
					Flight flight = objectMapper.treeToValue(jsonFlight, Flight.class);
					flights.add(flight);
				}
				return flights;
			}

			else {
				System.out.println("Failed to retrieve data. Response Code: " + responseCode);
				return null;
			}

		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}