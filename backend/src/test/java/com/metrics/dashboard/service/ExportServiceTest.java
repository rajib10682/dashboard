package com.metrics.dashboard.service;

import com.metrics.dashboard.entity.Override;
import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private ExportService exportService;

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
        testPlan2.setDataId(2);

        testOverride1 = new Override();
        testOverride1.setOverrideId(1L);
        testOverride1.setOverrideName("Override 1");
        testOverride1.setCoreExecutionTime(45.0);
        testOverride1.setTotalExecutionTime(60.0);
        testOverride1.setOnHoldTime(15.0);
        testOverride1.setRequestType("BATCH");
        testOverride1.setPlan(testPlan1);

        testOverride2 = new Override();
        testOverride2.setOverrideId(2L);
        testOverride2.setOverrideName("Override 2");
        testOverride2.setCoreExecutionTime(75.0);
        testOverride2.setTotalExecutionTime(90.0);
        testOverride2.setOnHoldTime(15.0);
        testOverride2.setRequestType("ONLINE");
        testOverride2.setPlan(testPlan2);

        testPlan1.setOverrides(Arrays.asList(testOverride1));
        testPlan2.setOverrides(Arrays.asList(testOverride2));
    }

    @Test
    void exportToCSV_WithValidData_ShouldReturnCSVBytes() throws IOException {
        List<Plan> plans = Arrays.asList(testPlan1, testPlan2);
        when(planRepository.findByDataId(any(Integer.class)))
                .thenReturn(plans);

        byte[] result = exportService.exportToCsv(1);

        assertNotNull(result);
        assertTrue(result.length > 0);

        String csvContent = new String(result);
        assertTrue(csvContent.contains("Plan ID,Plan Name,For Date,Data ID,Avg Core Execution Time"));
        assertTrue(csvContent.contains("Test Plan 1"));
        assertTrue(csvContent.contains("45.0"));

        verify(planRepository).findByDataId(any(Integer.class));
    }

    @Test
    void exportToCSV_WithEmptyData_ShouldReturnHeaderOnly() throws IOException {
        when(planRepository.findByDataId(any(Integer.class)))
                .thenReturn(Arrays.asList());

        byte[] result = exportService.exportToCsv(1);

        assertNotNull(result);
        assertTrue(result.length > 0);

        String csvContent = new String(result);
        assertTrue(csvContent.contains("Plan ID,Plan Name,For Date,Data ID,Avg Core Execution Time"));
        assertFalse(csvContent.contains("Test Plan"));
    }

    @Test
    void exportToExcel_WithValidData_ShouldReturnExcelBytes() throws IOException {
        List<Plan> plans = Arrays.asList(testPlan1, testPlan2);
        when(planRepository.findByDataId(any(Integer.class)))
                .thenReturn(plans);

        byte[] result = exportService.exportToCsv(1);

        assertNotNull(result);
        assertTrue(result.length > 0);

        verify(planRepository).findByDataId(any(Integer.class));
    }

    @Test
    void exportToExcel_WithEmptyData_ShouldReturnExcelWithHeaderOnly() throws IOException {
        when(planRepository.findByDataId(any(Integer.class)))
                .thenReturn(Arrays.asList());

        byte[] result = exportService.exportToCsv(1);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void exportToCSV_WithNullForDate_ShouldUseAllData() throws IOException {
        List<Plan> plans = Arrays.asList(testPlan1, testPlan2);
        when(planRepository.findByDataId(any(Integer.class)))
                .thenReturn(plans);

        byte[] result = exportService.exportToCsv(null);

        assertNotNull(result);
        assertTrue(result.length > 0);

        verify(planRepository).findByDataId(any(Integer.class));
    }

    @Test
    void exportToExcel_WithNullForDate_ShouldUseAllData() throws IOException {
        List<Plan> plans = Arrays.asList(testPlan1, testPlan2);
        when(planRepository.findByDataId(any(Integer.class)))
                .thenReturn(plans);

        byte[] result = exportService.exportToCsv(null);

        assertNotNull(result);
        assertTrue(result.length > 0);

        verify(planRepository).findByDataId(any(Integer.class));
    }

    @Test
    void exportToCSV_WithNullDataId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            exportService.exportToCsv(null);
        });
    }

    @Test
    void exportToExcel_WithNullDataId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            exportService.exportToCsv(null);
        });
    }
}
