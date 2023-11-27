package org.data_chain.adsbAircraft;

import java.util.Date;
import java.util.List;

public class Flights {

	public final List<Flight> flightsArray;
	public final Date timeOfRequest;

	public Flights(List<Flight> flights, long timeOfRequest) {
		this.flightsArray = flights;
		this.timeOfRequest = new Date(timeOfRequest);
	}

}