package com.metrics.dashboard.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    private Config config;

    @BeforeEach
    void setUp() {
        config = new Config();
    }

    @Test
    void testConfigCreation() {
        config.setId(1L);
        config.setRedThreshold(80.0);
        config.setAmberThreshold(60.0);
        config.setGreenThreshold(40.0);

        assertEquals(1L, config.getId());
        assertEquals(80.0, config.getRedThreshold());
        assertEquals(60.0, config.getAmberThreshold());
        assertEquals(40.0, config.getGreenThreshold());
    }

    @Test
    void testConfigConstructor() {
        Config constructedConfig = new Config(80.0, 60.0, 40.0);

        assertEquals(80.0, constructedConfig.getRedThreshold());
        assertEquals(60.0, constructedConfig.getAmberThreshold());
        assertEquals(40.0, constructedConfig.getGreenThreshold());
    }

    @Test
    void testConfigEquality() {
        Config config1 = new Config();
        config1.setId(1L);
        config1.setRedThreshold(80.0);

        Config config2 = new Config();
        config2.setId(1L);
        config2.setRedThreshold(80.0);

        assertEquals(config1.getId(), config2.getId());
        assertEquals(config1.getRedThreshold(), config2.getRedThreshold());
    }

    @Test
    void testConfigToString() {
        config.setId(1L);
        config.setRedThreshold(80.0);
        config.setAmberThreshold(60.0);
        config.setGreenThreshold(40.0);

        String configString = config.toString();
        assertNotNull(configString);
        assertTrue(configString.contains("80.0"));
    }
}
