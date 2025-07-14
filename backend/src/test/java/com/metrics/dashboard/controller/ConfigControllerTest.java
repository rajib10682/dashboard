package com.metrics.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.dashboard.dto.ThresholdConfig;
import com.metrics.dashboard.service.ConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConfigController.class)
class ConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfigService configService;

    @Autowired
    private ObjectMapper objectMapper;

    private ThresholdConfig testThresholds;

    @BeforeEach
    void setUp() {
        testThresholds = new ThresholdConfig();
        testThresholds.setRedThreshold(80.0);
        testThresholds.setAmberThreshold(60.0);
        testThresholds.setGreenThreshold(40.0);
    }

    @Test
    void getThresholds_ShouldReturnThresholdConfig() throws Exception {
        when(configService.getThresholds()).thenReturn(testThresholds);

        mockMvc.perform(get("/api/config/thresholds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.redThreshold").value(80.0))
                .andExpect(jsonPath("$.amberThreshold").value(60.0))
                .andExpect(jsonPath("$.greenThreshold").value(40.0));
    }

    @Test
    void updateThresholds_WithValidData_ShouldReturnUpdatedConfig() throws Exception {
        ThresholdConfig updatedThresholds = new ThresholdConfig();
        updatedThresholds.setRedThreshold(90.0);
        updatedThresholds.setAmberThreshold(70.0);
        updatedThresholds.setGreenThreshold(50.0);

        when(configService.updateThresholds(any(ThresholdConfig.class))).thenReturn(updatedThresholds);

        mockMvc.perform(put("/api/config/thresholds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedThresholds)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.redThreshold").value(90.0))
                .andExpect(jsonPath("$.amberThreshold").value(70.0))
                .andExpect(jsonPath("$.greenThreshold").value(50.0));
    }

    @Test
    void updateThresholds_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        ThresholdConfig invalidThresholds = new ThresholdConfig();
        invalidThresholds.setRedThreshold(-10.0);
        invalidThresholds.setAmberThreshold(60.0);
        invalidThresholds.setGreenThreshold(40.0);

        mockMvc.perform(put("/api/config/thresholds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidThresholds)))
                .andExpect(status().isBadRequest());
    }
}
