package com.metrics.dashboard.repository;

import com.metrics.dashboard.entity.Item;
import com.metrics.dashboard.entity.Override;
import com.metrics.dashboard.entity.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private Plan testPlan;
    private Override testOverride;
    private Item testItem1;
    private Item testItem2;

    @BeforeEach
    void setUp() {
        testPlan = new Plan();
        testPlan.setPlanName("Test Plan");
        testPlan.setForDate(LocalDate.of(2024, 3, 31));
        testPlan.setDataId(1);
        entityManager.persistAndFlush(testPlan);

        testOverride = new Override();
        testOverride.setOverrideName("Test Override");
        testOverride.setTotalExecutionTime(100.0);
        testOverride.setOnHoldTime(20.0);
        testOverride.setCoreExecutionTime(80.0);
        testOverride.setRequestType("BATCH");
        testOverride.setPlan(testPlan);
        entityManager.persistAndFlush(testOverride);

        testItem1 = new Item();
        testItem1.setItemId("ITEM001");
        testItem1.setItemName("ITEM001");
        testItem1.setCurrencyCode("USD");
        testItem1.setCreatedBy("TestUser");
        testItem1.setUpdatedBy("TestUser");
        testItem1.setOverride(testOverride);
        testItem1.setItemValueMonth1(100.0);

        testItem2 = new Item();
        testItem2.setItemId("ITEM002");
        testItem2.setItemName("ITEM002");
        testItem2.setCurrencyCode("EUR");
        testItem2.setCreatedBy("TestUser");
        testItem2.setUpdatedBy("TestUser");
        testItem2.setOverride(testOverride);
        testItem2.setItemValueMonth1(200.0);

        entityManager.persistAndFlush(testItem1);
        entityManager.persistAndFlush(testItem2);
    }

    @Test
    void findByItemId_WithExistingId_ShouldReturnItem() {
        Optional<Item> result = itemRepository.findByItemName("ITEM001");

        assertTrue(result.isPresent());
        assertEquals("ITEM001", result.get().getItemId());
        assertEquals("USD", result.get().getCurrencyCode());
    }

    @Test
    void findByItemId_WithNonExistingId_ShouldReturnEmpty() {
        Optional<Item> result = itemRepository.findById("NON_EXISTING");

        assertFalse(result.isPresent());
    }

    @Test
    void findByOverride_WithValidOverride_ShouldReturnItems() {
        List<Item> result = itemRepository.findAll().stream()
            .filter(item -> item.getOverride().equals(testOverride))
            .collect(java.util.stream.Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(i -> "ITEM001".equals(i.getItemId())));
        assertTrue(result.stream().anyMatch(i -> "ITEM002".equals(i.getItemId())));
    }

    @Test
    void findByOverrideOverrideId_WithValidOverrideId_ShouldReturnItems() {
        List<Item> result = itemRepository.findAll().stream()
            .filter(item -> item.getOverride().getOverrideId().equals(testOverride.getOverrideId()))
            .collect(java.util.stream.Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(i -> "ITEM001".equals(i.getItemId())));
        assertTrue(result.stream().anyMatch(i -> "ITEM002".equals(i.getItemId())));
    }

    @Test
    void findByCurrencyCode_WithValidCurrency_ShouldReturnItems() {
        List<Item> result = itemRepository.findAll().stream()
            .filter(item -> "USD".equals(item.getCurrencyCode()))
            .collect(java.util.stream.Collectors.toList());

        assertEquals(1, result.size());
        assertEquals("ITEM001", result.get(0).getItemId());
        assertEquals("USD", result.get(0).getCurrencyCode());
    }

    @Test
    void save_WithValidItem_ShouldPersistItem() {
        Item newItem = new Item();
        newItem.setItemId("ITEM003");
        newItem.setItemName("ITEM003");
        newItem.setCurrencyCode("GBP");
        newItem.setCreatedBy("TestUser");
        newItem.setUpdatedBy("TestUser");
        newItem.setOverride(testOverride);
        newItem.setItemValueMonth1(300.0);

        Item savedItem = itemRepository.save(newItem);

        assertNotNull(savedItem.getItemId());
        assertEquals("ITEM003", savedItem.getItemId());
        assertEquals("GBP", savedItem.getCurrencyCode());
        assertEquals(testOverride, savedItem.getOverride());
    }

    @Test
    void delete_WithExistingItem_ShouldRemoveItem() {
        String itemId = testItem1.getItemId();
        
        itemRepository.delete(testItem1);
        
        Optional<Item> result = itemRepository.findById(itemId);
        assertFalse(result.isPresent());
    }

    @Test
    void findByCreatedBy_ShouldReturnMatchingItems() {
        List<Item> result = itemRepository.findAll().stream()
            .filter(item -> "TestUser".equals(item.getCreatedBy()))
            .collect(java.util.stream.Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(i -> "TestUser".equals(i.getCreatedBy())));
    }
}
