package com.metrics.dashboard.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlanResponseTest {

    private PlanResponse planResponse;

    @BeforeEach
    void setUp() {
        planResponse = new PlanResponse();
    }

    @Test
    void testPlanResponseCreation() {
        planResponse.setPlanId(1L);
        planResponse.setPlanName("Test Plan");
        planResponse.setForDate(LocalDate.of(2024, 3, 31));
        planResponse.setDataId(1);
        planResponse.setAvgCoreExecutionTime(60.0);
        planResponse.setColorCode("GREEN");

        assertEquals(1L, planResponse.getPlanId());
        assertEquals("Test Plan", planResponse.getPlanName());
        assertEquals(LocalDate.of(2024, 3, 31), planResponse.getForDate());
        assertEquals(1, planResponse.getDataId());
        assertEquals(60.0, planResponse.getAvgCoreExecutionTime());
        assertEquals("GREEN", planResponse.getColorCode());
    }

    @Test
    void testPlanResponseEquality() {
        PlanResponse response1 = new PlanResponse();
        response1.setPlanId(1L);
        response1.setPlanName("Test Plan");

        PlanResponse response2 = new PlanResponse();
        response2.setPlanId(1L);
        response2.setPlanName("Test Plan");

        assertEquals(response1.getPlanId(), response2.getPlanId());
        assertEquals(response1.getPlanName(), response2.getPlanName());
    }

    @Test
    void testPlanResponseToString() {
        planResponse.setPlanId(1L);
        planResponse.setPlanName("Test Plan");
        planResponse.setColorCode("GREEN");

        String responseString = planResponse.toString();
        assertNotNull(responseString);
        assertTrue(responseString.contains("Test Plan"));
    }
}
