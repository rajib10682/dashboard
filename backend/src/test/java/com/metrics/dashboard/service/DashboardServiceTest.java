package com.metrics.dashboard.service;

import com.metrics.dashboard.dto.PlanResponse;
import com.metrics.dashboard.dto.SummaryResponse;
import com.metrics.dashboard.dto.ThresholdConfig;
import com.metrics.dashboard.entity.Override;
import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private ConfigService configService;
    
    @Mock
    private com.metrics.dashboard.repository.ConfigRepository configRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private Plan testPlan1;
    private Plan testPlan2;
    private Override testOverride1;
    private Override testOverride2;
    private ThresholdConfig testThresholds;

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
        testPlan2.setDataId(2);

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
        testOverride2.setPlan(testPlan1);

        testPlan1.setOverrides(Arrays.asList(testOverride1, testOverride2));
        testPlan2.setOverrides(Arrays.asList());

        testThresholds = new ThresholdConfig();
        testThresholds.setRedThreshold(80.0);
        testThresholds.setAmberThreshold(60.0);
        testThresholds.setGreenThreshold(40.0);
    }

    @Test
    void getDashboardData_WithValidParameters_ShouldReturnPagedResults() {
        List<Plan> plans = Arrays.asList(testPlan1, testPlan2);
        Page<Plan> planPage = new PageImpl<>(plans, PageRequest.of(0, 10), 2);
        
        when(planRepository.findByDataId(any(Integer.class), any(Pageable.class)))
                .thenReturn(planPage);

        Page<PlanResponse> result = dashboardService.getDashboardData(1, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        
        PlanResponse firstPlan = result.getContent().get(0);
        assertEquals("Test Plan 1", firstPlan.getPlanName());
        assertEquals(60.0, firstPlan.getAvgCoreExecutionTime());
        assertEquals("amber", firstPlan.getColorCode());
    }

    @Test
    void getDashboardData_WithNullForDate_ShouldUseDefaultDate() {
        List<Plan> plans = Arrays.asList(testPlan1);
        Page<Plan> planPage = new PageImpl<>(plans, PageRequest.of(0, 10), 1);
        
        when(planRepository.findByDataId(any(Integer.class), any(Pageable.class)))
                .thenReturn(planPage);

        Page<PlanResponse> result = dashboardService.getDashboardData(1, PageRequest.of(0, 10));

        assertNotNull(result);
        verify(planRepository).findByDataId(any(Integer.class), any(Pageable.class));
    }

    @Test
    void getSummaryData_WithValidParameters_ShouldReturnSummary() {
        List<Plan> plans = Arrays.asList(testPlan1, testPlan2);
        
        when(planRepository.findByDataId(any(Integer.class)))
                .thenReturn(plans);
        when(configService.getThresholds()).thenReturn(testThresholds);

        SummaryResponse result = dashboardService.getDashboardSummary(1);

        assertNotNull(result);
        assertEquals(2, result.getTotalPlans());
        assertEquals(1, result.getTotalItems());
        assertEquals(60.0, result.getAvgMedianTime());

        verify(planRepository).findByDataId(any(Integer.class));
    }

    @Test
    void getSummaryData_WithEmptyResults_ShouldReturnZeroSummary() {
        when(planRepository.findByDataId(any(Integer.class)))
                .thenReturn(Arrays.asList());

        SummaryResponse result = dashboardService.getDashboardSummary(1);

        assertNotNull(result);
        assertEquals(0, result.getTotalPlans());
        assertEquals(0, result.getTotalItems());
        assertEquals(0.0, result.getAvgMedianTime());
    }

}
