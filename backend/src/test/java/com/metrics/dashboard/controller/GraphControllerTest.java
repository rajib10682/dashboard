package com.metrics.dashboard.controller;

import com.metrics.dashboard.dto.TrendDataPoint;
import com.metrics.dashboard.service.GraphService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GraphController.class)
class GraphControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GraphService graphService;

    private List<TrendDataPoint> testTrendData;

    @BeforeEach
    void setUp() {
        TrendDataPoint point1 = new TrendDataPoint();
        point1.setPeriod("Q1 2024");
        point1.setMedianValue(45.0);

        TrendDataPoint point2 = new TrendDataPoint();
        point2.setPeriod("Q2 2024");
        point2.setMedianValue(55.0);

        testTrendData = Arrays.asList(point1, point2);
    }

    @Test
    void getQuarterlyTrends_WithValidDataId_ShouldReturnTrendData() throws Exception {
        when(graphService.getQuarterlyTrends(1)).thenReturn(testTrendData);

        mockMvc.perform(get("/api/graph/quarterly")
                .param("dataId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].period").value("Q1 2024"))
                .andExpect(jsonPath("$[0].medianValue").value(45.0))
                .andExpect(jsonPath("$[1].period").value("Q2 2024"))
                .andExpect(jsonPath("$[1].medianValue").value(55.0));
    }

    @Test
    void getDailyTrends_WithValidParameters_ShouldReturnTrendData() throws Exception {
        when(graphService.getDailyTrends(anyInt())).thenReturn(testTrendData);

        mockMvc.perform(get("/api/graph/daily")
                .param("dataId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].period").value("Q1 2024"))
                .andExpect(jsonPath("$[0].medianValue").value(45.0));
    }

    @Test
    void getQuarterlyTrends_WithInvalidDataId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/graph/quarterly")
                .param("dataId", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDailyTrends_WithInvalidDataId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/graph/daily")
                .param("dataId", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDailyTrends_WithMissingParameters_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/graph/daily"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getQuarterlyTrends_WithMissingDataId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/graph/quarterly"))
                .andExpect(status().isBadRequest());
    }
}
