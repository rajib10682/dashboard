package com.metrics.dashboard.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    private Plan plan;

    @BeforeEach
    void setUp() {
        plan = new Plan();
    }

    @Test
    void testPlanCreation() {
        plan.setPlanId(1L);
        plan.setPlanName("Test Plan");
        plan.setForDate(LocalDate.of(2024, 3, 31));
        plan.setDataId(1);

        assertEquals(1L, plan.getPlanId());
        assertEquals("Test Plan", plan.getPlanName());
        assertEquals(LocalDate.of(2024, 3, 31), plan.getForDate());
        assertEquals(1, plan.getDataId());
    }

    @Test
    void testPlanWithOverrides() {
        Override override1 = new Override();
        override1.setOverrideId(1L);
        override1.setOverrideName("Override 1");

        Override override2 = new Override();
        override2.setOverrideId(2L);
        override2.setOverrideName("Override 2");

        plan.setOverrides(Arrays.asList(override1, override2));

        assertNotNull(plan.getOverrides());
        assertEquals(2, plan.getOverrides().size());
        assertEquals("Override 1", plan.getOverrides().get(0).getOverrideName());
        assertEquals("Override 2", plan.getOverrides().get(1).getOverrideName());
    }

    @Test
    void testPlanEquality() {
        Plan plan1 = new Plan();
        plan1.setPlanId(1L);
        plan1.setPlanName("Test Plan");

        Plan plan2 = new Plan();
        plan2.setPlanId(1L);
        plan2.setPlanName("Test Plan");

        assertEquals(plan1.getPlanId(), plan2.getPlanId());
        assertEquals(plan1.getPlanName(), plan2.getPlanName());
    }

    @Test
    void testPlanToString() {
        plan.setPlanId(1L);
        plan.setPlanName("Test Plan");
        plan.setForDate(LocalDate.of(2024, 3, 31));

        String planString = plan.toString();
        assertNotNull(planString);
        assertTrue(planString.contains("Plan"));
    }

    @Test
    void testPlanConstructor() {
        Plan constructedPlan = new Plan("Test Plan", LocalDate.of(2024, 6, 30), 2);

        assertEquals("Test Plan", constructedPlan.getPlanName());
        assertEquals(LocalDate.of(2024, 6, 30), constructedPlan.getForDate());
        assertEquals(2, constructedPlan.getDataId());
    }

}
