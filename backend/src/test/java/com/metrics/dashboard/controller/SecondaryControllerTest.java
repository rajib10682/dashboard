package com.metrics.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.dashboard.entity.Override;
import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.exception.EntityNotFoundException;
import com.metrics.dashboard.repository.OverrideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SecondaryController.class)
class SecondaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OverrideRepository overrideRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Override testOverride;
    private Plan testPlan;

    @BeforeEach
    void setUp() {
        testPlan = new Plan();
        testPlan.setPlanId(1L);
        testPlan.setPlanName("Test Plan");
        testPlan.setForDate(LocalDate.of(2024, 3, 31));
        testPlan.setDataId(1);

        testOverride = new Override();
        testOverride.setOverrideId(1L);
        testOverride.setOverrideName("Test Override");
        testOverride.setTotalExecutionTime(100.0);
        testOverride.setOnHoldTime(20.0);
        testOverride.setCoreExecutionTime(80.0);
        testOverride.setRequestType("BATCH");
        testOverride.setPlan(testPlan);
    }

    @Test
    void getAllOverrides_ShouldReturnListOfOverrides() throws Exception {
        when(overrideRepository.findAll()).thenReturn(Arrays.asList(testOverride));

        mockMvc.perform(get("/api/secondary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].overrideName").value("Test Override"))
                .andExpect(jsonPath("$[0].totalExecutionTime").value(100.0))
                .andExpect(jsonPath("$[0].coreExecutionTime").value(80.0));
    }

    @Test
    void getOverride_WithValidId_ShouldReturnOverride() throws Exception {
        when(overrideRepository.findById(1L)).thenReturn(Optional.of(testOverride));

        mockMvc.perform(get("/api/secondary/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.overrideName").value("Test Override"))
                .andExpect(jsonPath("$.totalExecutionTime").value(100.0));
    }

    @Test
    void getOverride_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(overrideRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/secondary/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOverride_WithValidData_ShouldReturnCreatedOverride() throws Exception {
        when(overrideRepository.save(any(Override.class))).thenReturn(testOverride);

        mockMvc.perform(post("/api/secondary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOverride)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.overrideName").value("Test Override"));
    }

    @Test
    void updateOverride_WithValidData_ShouldReturnUpdatedOverride() throws Exception {
        when(overrideRepository.findById(1L)).thenReturn(Optional.of(testOverride));
        when(overrideRepository.save(any(Override.class))).thenReturn(testOverride);

        Override updatedOverride = new Override();
        updatedOverride.setOverrideName("Updated Override");
        updatedOverride.setTotalExecutionTime(120.0);
        updatedOverride.setOnHoldTime(25.0);
        updatedOverride.setCoreExecutionTime(95.0);
        updatedOverride.setRequestType("ONLINE");

        mockMvc.perform(put("/api/secondary/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedOverride)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateOverride_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(overrideRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/secondary/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOverride)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOverride_WithValidId_ShouldReturnOk() throws Exception {
        when(overrideRepository.findById(1L)).thenReturn(Optional.of(testOverride));

        mockMvc.perform(delete("/api/secondary/1"))
                .andExpect(status().isOk());

        verify(overrideRepository).deleteById(1L);
    }

    @Test
    void deleteOverride_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(overrideRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/secondary/999"))
                .andExpect(status().isNotFound());
    }
}
