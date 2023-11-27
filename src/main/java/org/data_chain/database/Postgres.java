package org.data_chain.database;

import org.data_chain.adsbAircraft.Flight;

import java.util.Date;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;


public class Postgres {

	private final String url;
	private final String user;
	private final String password;

	/**
     * Initialises Postgres setting instance variables.
     *
     * @param url     	 The url of the postgres server.
     * @param user    	 The username to authenticate to the server.
     * @param password   The password to authenticate to the server.
     */
	public Postgres(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     * @throws java.sql.SQLException
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Gets adsb request data
     * 
     * @param user 	the name of the user requesting the data
     * @return 		AdsbRequestData (requestCount, resetDate, resetOccurred) 
     */
    public AdsbRequestData getAdsbRequestData(String user) {
        String SQL = "SELECT * FROM adbs_exchange_request_tracker WHERE \"user\" = '" + user + "'";
     	AdsbRequestData adsbRequestData = null;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            rs.next();
            int requestCount = rs.getInt("request_count");
            int resetDate = rs.getInt("reset_date");
            boolean resetOccurred = rs.getBoolean("reset_occured");

            adsbRequestData = new AdsbRequestData(requestCount, resetDate, resetOccurred);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return adsbRequestData;
    }

    /**
	 * Updates the request_count back to 0.
	 * 
	 * @param user The name of the user.
	 */
	public Integer updateRequestCountToZero(String user) {
	    String SQL = "UPDATE adbs_exchange_request_tracker SET request_count = 0 WHERE \"user\" = ?";
	    
	    try (Connection conn = connect();
	         PreparedStatement pstmt = conn.prepareStatement(SQL)) {

	        pstmt.setString(1, user);
	        pstmt.executeUpdate();
	        pstmt.close();


	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }

        return 0;
	}

    /**
	 * Updates the reset_occured column in the adbs_exchange_request_tracker table for a specific user.
	 * 
	 * @param user The name of the user.
	 */
	public void updateResetOccurredToFalse(String user) {
	    String SQL = "UPDATE adbs_exchange_request_tracker SET reset_occured = false WHERE \"user\" = ?";
	    
	    try (Connection conn = connect();
	         PreparedStatement pstmt = conn.prepareStatement(SQL)) {

	        pstmt.setString(1, user);
	        pstmt.executeUpdate();
	        pstmt.close();

	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
	}

	/**
	 * Increments the request count by 1.
	 *
	 * @param user The name of the user.
	 */
	public void updateRequestCountByOne(String user) {
	    String SQL = "UPDATE adbs_exchange_request_tracker " +
	                 "SET request_count = request_count + 1 " +
	                 "WHERE \"user\" = ? ";

	    try (Connection conn = connect();
	         PreparedStatement pstmt = conn.prepareStatement(SQL)) {

	        pstmt.setString(1, user);
	        pstmt.executeUpdate();
	        pstmt.close();

	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
	}

	/**
	 * Inserts list of Flight object into the flights table along with time of request.
	 *
	 */
	public void createFlights(List<Flight> flights, Date timeOfRequest) {
	    String SQL = "INSERT INTO flight (hex, type, flight, registration, " +
	    			 "aircraft_type, altitude_baro, track, squawk, emergency, category, " +
	    			 "latitude, longitude, nic, rc, messages, distance, direction, time_of_request) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    try (Connection conn = connect();
	         PreparedStatement pstmt = conn.prepareStatement(SQL)) {

	    	for (Flight flight : flights) {

	            pstmt.setString(1, flight.hex());
	            pstmt.setString(2, flight.type());
	            pstmt.setString(3, flight.flight());
	            pstmt.setString(4, flight.registration());
	            pstmt.setString(5, flight.aircraftType());

	            if (flight.altitudeBaro() != null) { pstmt.setInt(6, flight.altitudeBaro()); } 
	            else { pstmt.setNull(6, Types.INTEGER); }

			    if (flight.track() != null) { pstmt.setDouble(7, flight.track()); } 
			    else { pstmt.setNull(7, Types.DOUBLE); }

			    pstmt.setString(8, flight.squawk());
			    pstmt.setString(9, flight.emergency());
			    pstmt.setString(10, flight.category());

			    if (flight.latitude() != null) { pstmt.setDouble(11, flight.latitude()); } 
			    else { pstmt.setNull(11, Types.DOUBLE); }

			    if (flight.longitude() != null) { pstmt.setDouble(12, flight.longitude()); } 
			    else { pstmt.setNull(12, Types.DOUBLE); }

			    if (flight.nic() != null) { pstmt.setInt(13, flight.nic()); } 
			    else { pstmt.setNull(13, Types.INTEGER); }

			    if (flight.rc() != null) { pstmt.setInt(14, flight.rc()); } 
			    else { pstmt.setNull(14, Types.INTEGER); }

			    if (flight.messages() != null) { pstmt.setInt(15, flight.messages()); } 
			    else { pstmt.setNull(15, Types.INTEGER); }

			    if (flight.dst() != null) { pstmt.setDouble(16, flight.dst()); } 
			    else { pstmt.setNull(16, Types.DOUBLE); }

			    if (flight.dir() != null) { pstmt.setDouble(17, flight.dir()); } 
			    else { pstmt.setNull(17, Types.DOUBLE); }

		    	pstmt.setTimestamp(18, new java.sql.Timestamp(timeOfRequest.getTime()));

		    	pstmt.addBatch();

	    	}

            pstmt.executeBatch();
            pstmt.close();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } 
	}
}