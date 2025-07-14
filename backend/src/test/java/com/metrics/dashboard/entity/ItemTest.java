package com.metrics.dashboard.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item item;
    private Override testOverride;

    @BeforeEach
    void setUp() {
        item = new Item();
        testOverride = new Override();
        testOverride.setOverrideId(1L);
        testOverride.setOverrideName("Test Override");
    }

    @Test
    void testItemCreation() {
        item.setItemId("ITEM001");
        item.setCurrencyCode("USD");
        item.setCreatedBy("TestUser");
        item.setUpdatedBy("TestUser");
        item.setCreateTimestamp(LocalDateTime.now());
        item.setUpdatedTimestamp(LocalDateTime.now());
        item.setOverride(testOverride);

        assertEquals("ITEM001", item.getItemId());
        assertEquals("USD", item.getCurrencyCode());
        assertEquals("TestUser", item.getCreatedBy());
        assertEquals("TestUser", item.getUpdatedBy());
        assertNotNull(item.getCreateTimestamp());
        assertNotNull(item.getUpdatedTimestamp());
        assertEquals(testOverride, item.getOverride());
    }

    @Test
    void testItemMonthlyValues() {
        item.setItemValueMonth1(100.0);
        item.setItemValueMonth2(200.0);
        item.setItemValueMonth3(300.0);
        item.setItemValueMonth4(400.0);
        item.setItemValueMonth5(500.0);
        item.setItemValueMonth6(600.0);
        item.setItemValueMonth7(700.0);
        item.setItemValueMonth8(800.0);
        item.setItemValueMonth9(900.0);
        item.setItemValueMonth10(1000.0);
        item.setItemValueMonth11(1100.0);
        item.setItemValueMonth12(1200.0);
        item.setItemValueMonth13(1300.0);
        item.setItemValueMonth14(1400.0);
        item.setItemValueMonth15(1500.0);
        item.setItemValueMonth16(1600.0);
        item.setItemValueMonth17(1700.0);
        item.setItemValueMonth18(1800.0);
        item.setItemValueMonth19(1900.0);
        item.setItemValueMonth20(2000.0);

        assertEquals(100.0, item.getItemValueMonth1());
        assertEquals(200.0, item.getItemValueMonth2());
        assertEquals(300.0, item.getItemValueMonth3());
        assertEquals(400.0, item.getItemValueMonth4());
        assertEquals(500.0, item.getItemValueMonth5());
        assertEquals(600.0, item.getItemValueMonth6());
        assertEquals(700.0, item.getItemValueMonth7());
        assertEquals(800.0, item.getItemValueMonth8());
        assertEquals(900.0, item.getItemValueMonth9());
        assertEquals(1000.0, item.getItemValueMonth10());
        assertEquals(1100.0, item.getItemValueMonth11());
        assertEquals(1200.0, item.getItemValueMonth12());
        assertEquals(1300.0, item.getItemValueMonth13());
        assertEquals(1400.0, item.getItemValueMonth14());
        assertEquals(1500.0, item.getItemValueMonth15());
        assertEquals(1600.0, item.getItemValueMonth16());
        assertEquals(1700.0, item.getItemValueMonth17());
        assertEquals(1800.0, item.getItemValueMonth18());
        assertEquals(1900.0, item.getItemValueMonth19());
        assertEquals(2000.0, item.getItemValueMonth20());
    }

    @Test
    void testItemConstructor() {
        Item constructedItem = new Item("ITEM001", testOverride, "USD", "TestUser", "TestUser", "Test Item");

        assertEquals(testOverride, constructedItem.getOverride());
        assertEquals("ITEM001", constructedItem.getItemId());
        assertEquals("USD", constructedItem.getCurrencyCode());
        assertEquals("TestUser", constructedItem.getCreatedBy());
        assertEquals("TestUser", constructedItem.getUpdatedBy());
        assertEquals("Test Item", constructedItem.getItemName());
        assertNotNull(constructedItem.getCreateTimestamp());
        assertNotNull(constructedItem.getUpdatedTimestamp());
    }

    @Test
    void testItemEquality() {
        Item item1 = new Item();
        item1.setItemId("ITEM001");
        item1.setCurrencyCode("USD");

        Item item2 = new Item();
        item2.setItemId("ITEM001");
        item2.setCurrencyCode("USD");

        assertEquals(item1.getItemId(), item2.getItemId());
        assertEquals(item1.getCurrencyCode(), item2.getCurrencyCode());
    }

    @Test
    void testItemToString() {
        item.setItemId("ITEM001");
        item.setCurrencyCode("USD");
        item.setCreatedBy("TestUser");

        String itemString = item.toString();
        assertNotNull(itemString);
        assertTrue(itemString.contains("Item"));
    }
}
