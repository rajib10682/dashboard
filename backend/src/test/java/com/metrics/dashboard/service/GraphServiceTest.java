package com.metrics.dashboard.service;

import com.metrics.dashboard.dto.TrendDataPoint;
import com.metrics.dashboard.entity.Override;
import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GraphServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private GraphService graphService;

    private Plan testPlan1;
    private Plan testPlan2;
    private Override testOverride1;
    private Override testOverride2;

    @BeforeEach
    void setUp() {
        testPlan1 = new Plan();
        testPlan1.setPlanId(1L);
        testPlan1.setPlanName("Test Plan 1");
        testPlan1.setForDate(LocalDate.of(2024, 3, 31));
        testPlan1.setDataId(1);

        testPlan2 = new Plan();
        testPlan2.setPlanId(2L);
        testPlan2.setPlanName("Test Plan 2");
        testPlan2.setForDate(LocalDate.of(2024, 6, 30));
        testPlan2.setDataId(1);

        testOverride1 = new Override();
        testOverride1.setOverrideId(1L);
        testOverride1.setOverrideName("Override 1");
        testOverride1.setCoreExecutionTime(45.0);
        testOverride1.setTotalExecutionTime(60.0);
        testOverride1.setOnHoldTime(15.0);
        testOverride1.setPlan(testPlan1);

        testOverride2 = new Override();
        testOverride2.setOverrideId(2L);
        testOverride2.setOverrideName("Override 2");
        testOverride2.setCoreExecutionTime(75.0);
        testOverride2.setTotalExecutionTime(90.0);
        testOverride2.setOnHoldTime(15.0);
        testOverride2.setPlan(testPlan2);

        testPlan1.setOverrides(Arrays.asList(testOverride1));
        testPlan2.setOverrides(Arrays.asList(testOverride2));
    }

    @Test
    void getQuarterlyTrends_WithValidDataId_ShouldReturnTrendData() {
        List<Plan> plans = Arrays.asList(testPlan1, testPlan2);
        when(planRepository.findByDataId(1)).thenReturn(plans);

        List<TrendDataPoint> result = graphService.getQuarterlyTrends(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        
        TrendDataPoint firstPoint = result.get(0);
        assertEquals("Q1 2024", firstPoint.getPeriod());
        assertEquals(45.0, firstPoint.getMedianValue());
        
        TrendDataPoint secondPoint = result.get(1);
        assertEquals("Q2 2024", secondPoint.getPeriod());
        assertEquals(75.0, secondPoint.getMedianValue());

        verify(planRepository).findByDataId(1);
    }

    @Test
    void getQuarterlyTrends_WithEmptyData_ShouldReturnEmptyList() {
        when(planRepository.findByDataId(1)).thenReturn(Arrays.asList());

        List<TrendDataPoint> result = graphService.getQuarterlyTrends(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(planRepository).findByDataId(1);
    }

    @Test
    void getDailyTrends_WithValidParameters_ShouldReturnTrendData() {
        List<Plan> plans = Arrays.asList(testPlan1);
        when(planRepository.findByDataId(1)).thenReturn(plans);

        List<TrendDataPoint> result = graphService.getDailyTrends(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        
        TrendDataPoint dataPoint = result.get(0);
        assertEquals("2024-03-31", dataPoint.getPeriod());
        assertEquals(45.0, dataPoint.getMedianValue());

        verify(planRepository).findByDataId(1);
    }

    @Test
    void getDailyTrends_WithEmptyData_ShouldReturnEmptyList() {
        when(planRepository.findByDataId(1))
                .thenReturn(Arrays.asList());

        List<TrendDataPoint> result = graphService.getDailyTrends(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getQuarterlyTrends_WithNullDataId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            graphService.getQuarterlyTrends(null);
        });
    }

    @Test
    void getDailyTrends_WithNullDataId_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            graphService.getDailyTrends(null);
        });
    }

    @Test
    void getQuarterlyTrends_WithPlansHavingNoOverrides_ShouldReturnZeroValues() {
        Plan planWithoutOverrides = new Plan();
        planWithoutOverrides.setPlanId(3L);
        planWithoutOverrides.setPlanName("Empty Plan");
        planWithoutOverrides.setForDate(LocalDate.of(2024, 9, 30));
        planWithoutOverrides.setDataId(1);
        planWithoutOverrides.setOverrides(Arrays.asList());

        when(planRepository.findByDataId(1)).thenReturn(Arrays.asList(planWithoutOverrides));

        List<TrendDataPoint> result = graphService.getQuarterlyTrends(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        
        TrendDataPoint dataPoint = result.get(0);
        assertEquals("Q3 2024", dataPoint.getPeriod());
        assertEquals(0.0, dataPoint.getMedianValue());
    }
}
