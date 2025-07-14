package com.metrics.dashboard.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThresholdConfigTest {

    private ThresholdConfig thresholdConfig;

    @BeforeEach
    void setUp() {
        thresholdConfig = new ThresholdConfig();
    }

    @Test
    void testThresholdConfigCreation() {
        thresholdConfig.setRedThreshold(80.0);
        thresholdConfig.setAmberThreshold(60.0);
        thresholdConfig.setGreenThreshold(40.0);

        assertEquals(80.0, thresholdConfig.getRedThreshold());
        assertEquals(60.0, thresholdConfig.getAmberThreshold());
        assertEquals(40.0, thresholdConfig.getGreenThreshold());
    }

    @Test
    void testThresholdConfigEquality() {
        ThresholdConfig config1 = new ThresholdConfig();
        config1.setRedThreshold(80.0);
        config1.setAmberThreshold(60.0);
        config1.setGreenThreshold(40.0);

        ThresholdConfig config2 = new ThresholdConfig();
        config2.setRedThreshold(80.0);
        config2.setAmberThreshold(60.0);
        config2.setGreenThreshold(40.0);

        assertEquals(config1.getRedThreshold(), config2.getRedThreshold());
        assertEquals(config1.getAmberThreshold(), config2.getAmberThreshold());
        assertEquals(config1.getGreenThreshold(), config2.getGreenThreshold());
    }

    @Test
    void testThresholdConfigToString() {
        thresholdConfig.setRedThreshold(80.0);
        thresholdConfig.setAmberThreshold(60.0);
        thresholdConfig.setGreenThreshold(40.0);

        String configString = thresholdConfig.toString();
        assertNotNull(configString);
        assertTrue(configString.contains("ThresholdConfig"));
    }
}
