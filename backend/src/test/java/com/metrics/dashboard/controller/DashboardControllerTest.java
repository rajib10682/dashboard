package com.metrics.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.dashboard.dto.PlanResponse;
import com.metrics.dashboard.dto.SummaryResponse;
import com.metrics.dashboard.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @Autowired
    private ObjectMapper objectMapper;

    private PlanResponse testPlanResponse;
    private SummaryResponse testSummaryResponse;

    @BeforeEach
    void setUp() {
        testPlanResponse = new PlanResponse();
        testPlanResponse.setPlanId(1L);
        testPlanResponse.setPlanName("Test Plan");
        testPlanResponse.setForDate(LocalDate.of(2024, 3, 31));
        testPlanResponse.setDataId(1);
        testPlanResponse.setAvgCoreExecutionTime(60.0);
        testPlanResponse.setColorCode("GREEN");

        testSummaryResponse = new SummaryResponse();
        testSummaryResponse.setTotalPlans(10);
        testSummaryResponse.setAvgMedianTime(65.5);
        testSummaryResponse.setTotalItems(25);
    }

    @Test
    void getDashboardData_WithValidParameters_ShouldReturnPagedData() throws Exception {
        Page<PlanResponse> mockPage = new PageImpl<>(
                Arrays.asList(testPlanResponse), 
                PageRequest.of(0, 10), 
                1);

        when(dashboardService.getDashboardData(any(Integer.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/dashboard")
                .param("forDate", "2024-03-31")
                .param("dataId", "1")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "planName")
                .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content[0].planName").value("Test Plan"))
                .andExpect(jsonPath("$.content[0].avgCoreExecutionTime").value(60.0))
                .andExpect(jsonPath("$.content[0].colorCode").value("GREEN"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getDashboardData_WithDefaultParameters_ShouldReturnData() throws Exception {
        Page<PlanResponse> mockPage = new PageImpl<>(
                Arrays.asList(testPlanResponse), 
                PageRequest.of(0, 10), 
                1);

        when(dashboardService.getDashboardData(any(Integer.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/dashboard")
                .param("dataId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content[0].planName").value("Test Plan"));
    }

    @Test
    void getSummaryData_WithValidParameters_ShouldReturnSummary() throws Exception {
        when(dashboardService.getDashboardSummary(any(Integer.class)))
                .thenReturn(testSummaryResponse);

        mockMvc.perform(get("/api/dashboard/summary")
                .param("forDate", "2024-03-31")
                .param("dataId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.totalPlans").value(10))
                .andExpect(jsonPath("$.totalItems").value(25))
                .andExpect(jsonPath("$.avgMedianTime").value(65.5));
    }

    @Test
    void getSummaryData_WithNullParameters_ShouldReturnSummary() throws Exception {
        when(dashboardService.getDashboardSummary(any(Integer.class)))
                .thenReturn(testSummaryResponse);

        mockMvc.perform(get("/api/dashboard/summary")
                .param("dataId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.totalPlans").value(10));
    }

    @Test
    void getDashboardData_WithInvalidDateFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/dashboard")
                .param("forDate", "invalid-date")
                .param("dataId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDashboardData_WithInvalidDataId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/dashboard")
                .param("forDate", "2024-03-31")
                .param("dataId", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDashboardData_WithNegativePage_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/dashboard")
                .param("dataId", "1")
                .param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDashboardData_WithInvalidSortDirection_ShouldUseDefaultSort() throws Exception {
        Page<PlanResponse> mockPage = new PageImpl<>(
                Arrays.asList(testPlanResponse), 
                PageRequest.of(0, 10), 
                1);

        when(dashboardService.getDashboardData(any(Integer.class), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/dashboard")
                .param("dataId", "1")
                .param("sortDir", "invalid"))
                .andExpect(status().isOk());
    }
}
