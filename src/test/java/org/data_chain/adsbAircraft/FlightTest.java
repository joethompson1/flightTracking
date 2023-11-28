package org.data_chain.adsbAircraft;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.Ignore;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test for simple App.
 */
public class FlightTest 
{
    @Test
    public void testFlightDeserialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON file from resources
        InputStream inputStream = getClass().getResourceAsStream("/org/data_chain/adsbAircraft/flightResponse-alt_baro-int.json");
        assertNotNull(inputStream);

        // Convert JSON to JsonNode
        JsonNode jsonNode = objectMapper.readTree(inputStream);
        assertNotNull(jsonNode);

        // Deserialize 'ac' array into List<Flight>
        List<Flight> flights = objectMapper.readValue(jsonNode.get("ac").toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Flight.class));

        // Verify that the list has one Flight object
        assertEquals(1, flights.size());

        // Verify specific properties of the Flight object
        Flight flight = flights.get(0);
        assertEquals("3e4719", flight.hex());
        assertEquals("adsb_icao", flight.type());
        assertEquals("DISCV", flight.flight());
        assertEquals("D-ISCV", flight.registration());
        assertEquals("C25A", flight.aircraftType());
        assertEquals(250.19D, (double) flight.track(), 0);
        assertEquals(3800L, (long) flight.altitudeBaro());
        assertEquals(51.487892D, (double) flight.latitude(), 0);
        assertEquals(0.462875D, (double) flight.longitude(), 0);
    }

    @Test
    public void testFlightDeserializationWhenBaroIsString() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON file from resources
        InputStream inputStream = getClass().getResourceAsStream("/org/data_chain/adsbAircraft/flightResponse-alt_baro-string.json");
        assertNotNull(inputStream);

        // Convert JSON to JsonNode
        JsonNode jsonNode = objectMapper.readTree(inputStream);
        assertNotNull(jsonNode);

        // Deserialize 'ac' array into List<Flight>
        List<Flight> flights = objectMapper.readValue(jsonNode.get("ac").toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Flight.class));

        // Verify that the list has one Flight object
        assertEquals(1, flights.size());

        // Verify specific properties of the Flight object
        Flight flight = flights.get(0);
        assertEquals("3e4719", flight.hex());
        assertEquals("adsb_icao", flight.type());
        assertEquals("DISCV", flight.flight());
        assertEquals(0L, (long) flight.altitudeBaro());
    }
}
