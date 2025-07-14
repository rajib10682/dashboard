package com.metrics.dashboard.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OverrideTest {

    private Override override;
    private Plan testPlan;

    @BeforeEach
    void setUp() {
        override = new Override();
        testPlan = new Plan();
        testPlan.setPlanId(1L);
        testPlan.setPlanName("Test Plan");
        testPlan.setForDate(LocalDate.of(2024, 3, 31));
        testPlan.setDataId(1);
    }

    @Test
    void testOverrideCreation() {
        override.setOverrideId(1L);
        override.setOverrideName("Test Override");
        override.setTotalExecutionTime(100.0);
        override.setOnHoldTime(20.0);
        override.setCoreExecutionTime(80.0);
        override.setRequestType("BATCH");
        override.setPlan(testPlan);

        assertEquals(1L, override.getOverrideId());
        assertEquals("Test Override", override.getOverrideName());
        assertEquals(100.0, override.getTotalExecutionTime());
        assertEquals(20.0, override.getOnHoldTime());
        assertEquals(80.0, override.getCoreExecutionTime());
        assertEquals("BATCH", override.getRequestType());
        assertEquals(testPlan, override.getPlan());
    }

    @Test
    void testOverrideConstructor() {
        Override constructedOverride = new Override(testPlan, "Test Override", 100.0, 20.0, 80.0, "BATCH");

        assertEquals(testPlan, constructedOverride.getPlan());
        assertEquals("Test Override", constructedOverride.getOverrideName());
        assertEquals(100.0, constructedOverride.getTotalExecutionTime());
        assertEquals(20.0, constructedOverride.getOnHoldTime());
        assertEquals(80.0, constructedOverride.getCoreExecutionTime());
        assertEquals("BATCH", constructedOverride.getRequestType());
    }

    @Test
    void testOverrideWithItems() {
        Item item1 = new Item();
        item1.setItemId("ITEM001");
        item1.setCurrencyCode("USD");

        Item item2 = new Item();
        item2.setItemId("ITEM002");
        item2.setCurrencyCode("EUR");

        override.setItems(Arrays.asList(item1, item2));

        assertNotNull(override.getItems());
        assertEquals(2, override.getItems().size());
        assertEquals("ITEM001", override.getItems().get(0).getItemId());
        assertEquals("ITEM002", override.getItems().get(1).getItemId());
    }

    @Test
    void testOverrideEquality() {
        Override override1 = new Override();
        override1.setOverrideId(1L);
        override1.setOverrideName("Test Override");

        Override override2 = new Override();
        override2.setOverrideId(1L);
        override2.setOverrideName("Test Override");

        assertEquals(override1.getOverrideId(), override2.getOverrideId());
        assertEquals(override1.getOverrideName(), override2.getOverrideName());
    }

    @Test
    void testOverrideToString() {
        override.setOverrideId(1L);
        override.setOverrideName("Test Override");
        override.setTotalExecutionTime(100.0);
        override.setCoreExecutionTime(80.0);

        String overrideString = override.toString();
        assertNotNull(overrideString);
        assertTrue(overrideString.contains("Override"));
    }
}
