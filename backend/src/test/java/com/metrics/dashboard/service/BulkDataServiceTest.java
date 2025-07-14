package com.metrics.dashboard.service;

import com.metrics.dashboard.entity.Item;
import com.metrics.dashboard.entity.Override;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkDataServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private OverrideRepository overrideRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BulkDataService bulkDataService;

    private Map<String, List<Map<String, Object>>> testExcelData;
    private Plan testPlan;
    private Override testOverride;

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
        testOverride.setPlan(testPlan);

        testExcelData = new HashMap<>();
        
        List<Map<String, Object>> planData = new ArrayList<>();
        Map<String, Object> planRow = new HashMap<>();
        planRow.put("Plan_Name", "Test Plan");
        planRow.put("For_Date", "31-Mar-2024");
        planRow.put("Data_ID", "1");
        planData.add(planRow);
        
        List<Map<String, Object>> overrideData = new ArrayList<>();
        Map<String, Object> overrideRow = new HashMap<>();
        overrideRow.put("Plan_Name", "Test Plan");
        overrideRow.put("Override_Name", "Test Override");
        overrideRow.put("Total_Execution_Time", "100.0");
        overrideRow.put("On_Hold_Time", "20.0");
        overrideRow.put("Core_Execution_Time", "80.0");
        overrideRow.put("Request_Type", "BATCH");
        overrideData.add(overrideRow);
        
        List<Map<String, Object>> itemData = new ArrayList<>();
        Map<String, Object> itemRow = new HashMap<>();
        itemRow.put("Override_Name", "Test Override");
        itemRow.put("Item_ID", "ITEM001");
        itemRow.put("Currency_Code", "USD");
        itemRow.put("Created_By", "TestUser");
        itemRow.put("Updated_By", "TestUser");
        for (int i = 1; i <= 20; i++) {
            itemRow.put("Month_" + i + "_Value", "100.0");
        }
        itemData.add(itemRow);
        
        testExcelData.put("Plans", planData);
        testExcelData.put("Overrides", overrideData);
        testExcelData.put("Items", itemData);
    }

    @Test
    void processBulkData_WithValidData_ShouldProcessSuccessfully() {
        when(planRepository.findByPlanName(anyString())).thenReturn(Optional.empty());
        when(planRepository.saveAll(any())).thenReturn(Arrays.asList(testPlan));
        when(overrideRepository.findByOverrideName(anyString())).thenReturn(Optional.empty());
        when(overrideRepository.saveAll(any())).thenReturn(Arrays.asList(testOverride));
        when(itemRepository.findByItemName(anyString())).thenReturn(Optional.empty());
        when(itemRepository.saveAll(any())).thenReturn(Arrays.asList(new Item()));

        Map<String, List<String>> result = bulkDataService.processBulkData(testExcelData);

        assertNotNull(result);
        assertTrue(result.containsKey("status"));
        assertTrue(result.containsKey("reason"));
        assertEquals(3, result.get("status").size());
        assertEquals("Success", result.get("status").get(0));
        assertEquals("Success", result.get("status").get(1));
        assertEquals("Success", result.get("status").get(2));

        verify(planRepository).saveAll(any());
        verify(overrideRepository).saveAll(any());
        verify(itemRepository).saveAll(any());
    }

    @Test
    void processBulkData_WithInvalidPlanData_ShouldReturnError() {
        Map<String, Object> invalidPlanRow = new HashMap<>();
        invalidPlanRow.put("Plan_Name", "");
        invalidPlanRow.put("For_Date", "31-Mar-2024");
        invalidPlanRow.put("Data_ID", "1");
        
        testExcelData.get("Plans").clear();
        testExcelData.get("Plans").add(invalidPlanRow);

        Map<String, List<String>> result = bulkDataService.processBulkData(testExcelData);

        assertNotNull(result);
        assertEquals("Error", result.get("status").get(0));
        assertTrue(result.get("reason").get(0).contains("Plan_Name is required"));
    }

    @Test
    void processBulkData_WithInvalidQuarterEndDate_ShouldReturnError() {
        Map<String, Object> invalidPlanRow = new HashMap<>();
        invalidPlanRow.put("Plan_Name", "Test Plan");
        invalidPlanRow.put("For_Date", "15-Mar-2024");
        invalidPlanRow.put("Data_ID", "1");
        
        testExcelData.get("Plans").clear();
        testExcelData.get("Plans").add(invalidPlanRow);

        Map<String, List<String>> result = bulkDataService.processBulkData(testExcelData);

        assertNotNull(result);
        assertEquals("Error", result.get("status").get(0));
        assertTrue(result.get("reason").get(0).contains("quarter"));
    }

    @Test
    void processBulkData_WithDeleteFlag_ShouldDeletePlan() {
        Map<String, Object> deletePlanRow = new HashMap<>();
        deletePlanRow.put("Plan_Name", "Test Plan");
        deletePlanRow.put("For_Date", "31-Mar-2024");
        deletePlanRow.put("Data_ID", "1");
        deletePlanRow.put("Delete", "D");
        
        testExcelData.get("Plans").clear();
        testExcelData.get("Plans").add(deletePlanRow);
        
        when(planRepository.findByPlanName(anyString())).thenReturn(Optional.of(testPlan));

        Map<String, List<String>> result = bulkDataService.processBulkData(testExcelData);

        assertNotNull(result);
        assertEquals("Success", result.get("status").get(0));
        assertTrue(result.get("reason").get(0).contains("deleted"));
        verify(planRepository).deleteAll(Arrays.asList(testPlan));
    }

    @Test
    void validatePlanData_WithValidData_ShouldReturnNull() {
        String result = bulkDataService.validatePlanData("Test Plan", "31-Mar-2024", "1");
        assertNull(result);
    }

    @Test
    void validatePlanData_WithEmptyPlanName_ShouldReturnError() {
        String result = bulkDataService.validatePlanData("", "31-Mar-2024", "1");
        assertEquals("Plan_Name is required", result);
    }

    @Test
    void validatePlanData_WithInvalidDate_ShouldReturnError() {
        String result = bulkDataService.validatePlanData("Test Plan", "invalid-date", "1");
        assertNotNull(result);
        assertTrue(result.contains("Invalid date format"));
    }

    @Test
    void validatePlanData_WithInvalidDataId_ShouldReturnError() {
        String result = bulkDataService.validatePlanData("Test Plan", "31-Mar-2024", "invalid");
        assertNotNull(result);
        assertTrue(result.contains("Data_ID must be a valid number"));
    }

    @Test
    void getString_WithValidKey_ShouldReturnValue() {
        Map<String, Object> data = new HashMap<>();
        data.put("test_key", "test_value");
        
        String result = bulkDataService.getString(data, "test_key");
        assertEquals("test_value", result);
    }

    @Test
    void getString_WithNullValue_ShouldReturnNull() {
        Map<String, Object> data = new HashMap<>();
        data.put("test_key", null);
        
        String result = bulkDataService.getString(data, "test_key");
        assertNull(result);
    }

    @Test
    void getString_WithMissingKey_ShouldReturnNull() {
        Map<String, Object> data = new HashMap<>();
        
        String result = bulkDataService.getString(data, "missing_key");
        assertNull(result);
    }
}
