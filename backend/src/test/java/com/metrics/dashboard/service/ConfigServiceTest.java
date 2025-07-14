package com.metrics.dashboard.service;

import com.metrics.dashboard.dto.ThresholdConfig;
import com.metrics.dashboard.entity.Config;
import com.metrics.dashboard.repository.ConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigServiceTest {

    @Mock
    private ConfigRepository configRepository;

    @InjectMocks
    private ConfigService configService;

    private Config testConfig;
    private ThresholdConfig testThresholdConfig;

    @BeforeEach
    void setUp() {
        testConfig = new Config();
        testConfig.setId(1L);
        testConfig.setRedThreshold(80.0);
        testConfig.setAmberThreshold(60.0);
        testConfig.setGreenThreshold(40.0);

        testThresholdConfig = new ThresholdConfig();
        testThresholdConfig.setRedThreshold(80.0);
        testThresholdConfig.setAmberThreshold(60.0);
        testThresholdConfig.setGreenThreshold(40.0);
    }

    @Test
    void getThresholds_WhenConfigExists_ShouldReturnThresholdConfig() {
        when(configRepository.findAll()).thenReturn(java.util.Arrays.asList(testConfig));

        ThresholdConfig result = configService.getThresholds();

        assertNotNull(result);
        assertEquals(80.0, result.getRedThreshold());
        assertEquals(60.0, result.getAmberThreshold());
        assertEquals(40.0, result.getGreenThreshold());
        verify(configRepository).findAll();
    }

    @Test
    void getThresholds_WhenConfigDoesNotExist_ShouldReturnDefaultThresholds() {
        when(configRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        ThresholdConfig result = configService.getThresholds();

        assertNotNull(result);
        assertEquals(100.0, result.getRedThreshold());
        assertEquals(50.0, result.getAmberThreshold());
        assertEquals(25.0, result.getGreenThreshold());
        verify(configRepository).findAll();
    }

    @Test
    void updateThresholds_WhenConfigExists_ShouldUpdateExistingConfig() {
        when(configRepository.findAll()).thenReturn(java.util.Arrays.asList(testConfig));
        when(configRepository.save(any(Config.class))).thenReturn(testConfig);

        ThresholdConfig newThresholds = new ThresholdConfig();
        newThresholds.setRedThreshold(90.0);
        newThresholds.setAmberThreshold(70.0);
        newThresholds.setGreenThreshold(50.0);

        ThresholdConfig result = configService.updateThresholds(newThresholds);

        assertNotNull(result);
        assertEquals(90.0, result.getRedThreshold());
        assertEquals(70.0, result.getAmberThreshold());
        assertEquals(50.0, result.getGreenThreshold());
        verify(configRepository).findAll();
        verify(configRepository).save(any(Config.class));
    }

    @Test
    void updateThresholds_WhenConfigDoesNotExist_ShouldCreateNewConfig() {
        when(configRepository.findAll()).thenReturn(java.util.Collections.emptyList());
        when(configRepository.save(any(Config.class))).thenReturn(testConfig);

        ThresholdConfig newThresholds = new ThresholdConfig();
        newThresholds.setRedThreshold(85.0);
        newThresholds.setAmberThreshold(65.0);
        newThresholds.setGreenThreshold(45.0);

        ThresholdConfig result = configService.updateThresholds(newThresholds);

        assertNotNull(result);
        verify(configRepository).findAll();
        verify(configRepository).save(any(Config.class));
    }

    @Test
    void updateThresholds_WithNullInput_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            configService.updateThresholds(null);
        });
    }

    @Test
    void updateThresholds_WithInvalidThresholds_ShouldThrowException() {
        ThresholdConfig invalidThresholds = new ThresholdConfig();
        invalidThresholds.setRedThreshold(-10.0);
        invalidThresholds.setAmberThreshold(60.0);
        invalidThresholds.setGreenThreshold(40.0);

        assertThrows(IllegalArgumentException.class, () -> {
            configService.updateThresholds(invalidThresholds);
        });
    }
}
