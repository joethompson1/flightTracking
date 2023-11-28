import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.data_chain.database.Postgres;
import org.data_chain.database.AdsbRequestData;
import org.data_chain.app.EnvVariables;


public class PostgresTest {

    private Postgres postgres;
    private Connection connection;
    private EnvVariables env;

    @Before
    public void setUp() throws Exception {
        String testConfigPath = "config/test-config.properties";
        // Load the EnvVariables with the test configuration
        env = new EnvVariables(testConfigPath);

        connection = DriverManager.getConnection(env.postgresURL, env.postgresUser, env.postgresPassword);

        // Create an instance of your Postgres class with the test database connection
        postgres = new Postgres(env.postgresURL, env.postgresUser, env.postgresPassword);

        // Clear tables in the test database before each test
        clearDatabase();
    }

    @Test
    public void testGetAdsbRequestData() throws Exception {

        int requestCount = 10;
        int resetDate = 17;
        boolean resetOccurred = false;

        // Insert test data into the table
        insertTestData(env.user, requestCount, resetDate, resetOccurred);

        AdsbRequestData result = postgres.getAdsbRequestData(env.user);

        assertEquals(requestCount, result.requestCount);
        assertEquals(resetDate, result.resetDate);
        assertEquals(resetOccurred, result.resetOccurred);
    }

    @Test
    public void testUpdateRequestCountToZero() throws Exception {

        int requestCount = 10;
        int resetDate = 17;
        boolean resetOccurred = false;

        // Insert test data into the table
        insertTestData(env.user, requestCount, resetDate, resetOccurred);

        postgres.updateRequestCountToZero(env.user);
        AdsbRequestData result = postgres.getAdsbRequestData(env.user);

        assertEquals(0, result.requestCount);
    }

    @Test
    public void testUpdateResetOccurredToFalse() throws Exception {

        int requestCount = 10;
        int resetDate = 17;
        boolean resetOccurred = true;

        // Insert test data into the table
        insertTestData(env.user, requestCount, resetDate, resetOccurred);

        postgres.updateResetOccurredToFalse(env.user);
        AdsbRequestData result = postgres.getAdsbRequestData(env.user);

        assertEquals(false, result.resetOccurred);
    }

    @Test
    public void testUpdateRequestCountByOne() throws Exception {

        int requestCount = 10;
        int resetDate = 17;
        boolean resetOccurred = true;

        // Insert test data into the table
        insertTestData(env.user, requestCount, resetDate, resetOccurred);

        postgres.updateRequestCountByOne(env.user);
        AdsbRequestData result = postgres.getAdsbRequestData(env.user);

        assertEquals(11, result.requestCount);
    }

    private void clearDatabase() throws Exception {
        clearTable("adbs_exchange_request_tracker");
        clearTable("flight");
    }

    private void clearTable(String tableName) throws Exception {
        String sql = "DELETE FROM " + tableName;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }

    private void insertTestData(String user, int requestCount, int resetDate, boolean resetOccurred) throws Exception {
        // Add code here to insert test data into the test database
        // You might want to execute SQL statements to insert data
        String sql = "INSERT INTO adbs_exchange_request_tracker (\"user\", request_count, reset_date, reset_occured) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setInt(2, requestCount);
            pstmt.setInt(3, resetDate);
            pstmt.setBoolean(4, resetOccurred);

            pstmt.executeUpdate();
        }
    }
}
