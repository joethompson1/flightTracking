package com.data_chain.adsbAircraft;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Flight(
        String hex,
        String type,
        String flight,
        @JsonProperty("r") String registration,
        @JsonProperty("t") String aircraftType,
        @JsonProperty("alt_baro") Integer altitudeBaro,
        Double track,
        String squawk,
        String emergency,
        String category,
        Double latitude,
        Double longitude,
        Integer nic,
        Integer rc,
        Integer messages,
        Double dst,
        Double dir
) {

    public static Flight createFlight(String hex, String type, String flight, String registration, String aircraftType,
                                      Object altitudeBaro, double track, String squawk, String emergency, 
                                      String category, double latitude, double longitude, int nic, int rc, 
                                      int messages, double dst, double dir) {

        Integer convertedAltitude = 0;

        if (altitudeBaro != null) {
            if (altitudeBaro instanceof Integer) {
                convertedAltitude = (Integer) altitudeBaro;
            }
        }
        
        return new Flight(hex, type, flight, registration, aircraftType, convertedAltitude, track, squawk,
                emergency, category, latitude, longitude, nic, rc, messages, dst, dir);
    }
}