package org.data_chain.app;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EnvVariablesTest {

    @Test
    public void testEnvVariablesLoading() {
        String testConfigPath = "config/test-config.properties";

        // Load the EnvVariables with the test configuration
        EnvVariables env = new EnvVariables(testConfigPath);

        // Add your test assertions here
        assertEquals("fakeUser", env.user);
        assertEquals("0000000000000", env.apiKey);
        assertEquals("https://adsbexchange-com1.p.rapidapi.com/v2", env.baseUrl);
        assertEquals(51.844991D, (double) env.latitude, 0);
        assertEquals(-1.294335D, (double) env.longitude, 0);
        assertEquals(135000L, (long) env.waitTime);
        assertEquals(250L, (long) env.radius);
        assertEquals(7L, (long) env.startTime);
        assertEquals(19L, (long) env.endTime);
        assertEquals(9990L, (long) env.maxNumberRequests);

        assertEquals("jdbc:postgresql://ep-calm-sky-11321164-pooler.eu-central-1.aws.neon.tech/test?sslmode=require&prepareThreshold=0", env.postgresURL);
        assertEquals("joethompson1", env.postgresUser);
    }
}