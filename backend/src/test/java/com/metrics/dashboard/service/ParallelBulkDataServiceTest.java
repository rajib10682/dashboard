package com.metrics.dashboard.service;

import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.ItemRepository;
import com.metrics.dashboard.repository.OverrideRepository;
import com.metrics.dashboard.repository.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParallelBulkDataServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private OverrideRepository overrideRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BulkDataService bulkDataService;

    @InjectMocks
    private ParallelBulkDataService parallelBulkDataService;

    private Map<String, List<Map<String, Object>>> testExcelData;
    private Plan testPlan;

    @BeforeEach
    void setUp() {
        testPlan = new Plan();
        testPlan.setPlanId(1L);
        testPlan.setPlanName("Test Plan");
        testPlan.setForDate(LocalDate.of(2024, 3, 31));
        testPlan.setDataId(1);

        testExcelData = new HashMap<>();
        
        List<Map<String, Object>> planData = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Map<String, Object> planRow = new HashMap<>();
            planRow.put("Plan_Name", "Test Plan " + i);
            planRow.put("For_Date", "31-Mar-2024");
            planRow.put("Data_ID", String.valueOf(i));
            planData.add(planRow);
        }
        
        testExcelData.put("Plans", planData);
        testExcelData.put("Overrides", new ArrayList<>());
        testExcelData.put("Items", new ArrayList<>());
    }

    @Test
    void processParallelBulkData_WithSmallDataset_ShouldUseSequentialProcessing() {
        Map<String, List<String>> expectedResult = new HashMap<>();
        expectedResult.put("status", Arrays.asList("Success"));
        expectedResult.put("reason", Arrays.asList("Plan created successfully"));

        when(bulkDataService.processBulkData(testExcelData)).thenReturn(expectedResult);

        Map<String, List<String>> result = parallelBulkDataService.processParallelBulkData(testExcelData);

        assertNotNull(result);
        assertEquals(expectedResult, result);
        verify(bulkDataService).processBulkData(testExcelData);
    }

    @Test
    void processParallelBulkData_WithLargeDataset_ShouldUseParallelProcessing() {
        List<Map<String, Object>> largePlanData = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            Map<String, Object> planRow = new HashMap<>();
            planRow.put("Plan_Name", "Test Plan " + i);
            planRow.put("For_Date", "31-Mar-2024");
            planRow.put("Data_ID", String.valueOf(i));
            largePlanData.add(planRow);
        }
        
        testExcelData.put("Plans", largePlanData);

        Map<String, List<String>> result = parallelBulkDataService.processParallelBulkData(testExcelData);

        assertNotNull(result);
        assertTrue(result.containsKey("status"));
        assertTrue(result.containsKey("reason"));
    }

    @Test
    void processPlanGroupAsync_WithValidPlanGroup_ShouldProcessSuccessfully() {
        List<Map<String, Object>> planGroup = new ArrayList<>();
        Map<String, Object> planRow = new HashMap<>();
        planRow.put("Plan_Name", "Test Plan");
        planRow.put("For_Date", "31-Mar-2024");
        planRow.put("Data_ID", "1");
        planGroup.add(planRow);

        CompletableFuture<Map<String, Object>> result = parallelBulkDataService.processPlanGroupAsync(planGroup, 0);

        assertNotNull(result);
        assertTrue(result.isDone());
        
        try {
            Map<String, Object> resultMap = result.get();
            assertNotNull(resultMap);
            assertTrue(resultMap.containsKey("status"));
            assertTrue(resultMap.containsKey("reason"));
            assertTrue(resultMap.containsKey("successfulPlans"));
        } catch (Exception e) {
            fail("CompletableFuture should complete successfully");
        }
    }

    @Test
    void processPlanGroupAsync_WithInvalidPlanData_ShouldReturnErrors() {
        List<Map<String, Object>> planGroup = new ArrayList<>();
        Map<String, Object> planRow = new HashMap<>();
        planRow.put("Plan_Name", "");
        planRow.put("For_Date", "31-Mar-2024");
        planRow.put("Data_ID", "1");
        planGroup.add(planRow);

        when(bulkDataService.getString(any(), anyString())).thenReturn("");
        when(bulkDataService.validatePlanData(anyString(), anyString(), anyString())).thenReturn("Plan_Name is required");

        CompletableFuture<Map<String, Object>> result = parallelBulkDataService.processPlanGroupAsync(planGroup, 0);

        assertNotNull(result);
        assertTrue(result.isDone());
        
        try {
            Map<String, Object> resultMap = result.get();
            List<String> statusList = (List<String>) resultMap.get("status");
            List<String> reasonList = (List<String>) resultMap.get("reason");
            
            assertEquals("Error", statusList.get(0));
            assertEquals("Plan_Name is required", reasonList.get(0));
        } catch (Exception e) {
            fail("CompletableFuture should complete successfully");
        }
    }

    @Test
    void processParallelBulkData_WithException_ShouldFallbackToSequential() {
        List<Map<String, Object>> largePlanData = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            Map<String, Object> planRow = new HashMap<>();
            planRow.put("Plan_Name", "Test Plan " + i);
            planRow.put("For_Date", "31-Mar-2024");
            planRow.put("Data_ID", String.valueOf(i));
            largePlanData.add(planRow);
        }
        
        testExcelData.put("Plans", largePlanData);

        when(bulkDataService.getString(any(), anyString())).thenThrow(new RuntimeException("Test exception"));
        
        Map<String, List<String>> fallbackResult = new HashMap<>();
        fallbackResult.put("status", Arrays.asList("Success"));
        fallbackResult.put("reason", Arrays.asList("Fallback processing"));
        when(bulkDataService.processBulkData(testExcelData)).thenReturn(fallbackResult);

        Map<String, List<String>> result = parallelBulkDataService.processParallelBulkData(testExcelData);

        assertNotNull(result);
        assertEquals(fallbackResult, result);
        verify(bulkDataService).processBulkData(testExcelData);
    }
}
