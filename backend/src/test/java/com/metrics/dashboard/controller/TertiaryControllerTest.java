package com.metrics.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.dashboard.entity.Item;
import com.metrics.dashboard.entity.Override;
import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.ItemRepository;
import com.metrics.dashboard.repository.OverrideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TertiaryController.class)
class TertiaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private OverrideRepository overrideRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Item testItem;
    private Override testOverride;
    private Plan testPlan;

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

        testItem = new Item();
        testItem.setItemId("ITEM001");
        testItem.setCurrencyCode("USD");
        testItem.setCreatedBy("TestUser");
        testItem.setUpdatedBy("TestUser");
        testItem.setOverride(testOverride);
        testItem.setItemValueMonth1(100.0);
    }

    @Test
    void getAllItems_ShouldReturnAllItems() throws Exception {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(testItem));

        mockMvc.perform(get("/api/tertiary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].itemId").value("ITEM001"))
                .andExpect(jsonPath("$[0].currencyCode").value("USD"));
    }

    @Test
    void getItemById_WithExistingId_ShouldReturnItem() throws Exception {
        when(itemRepository.findByItemName("ITEM001")).thenReturn(Optional.of(testItem));

        mockMvc.perform(get("/api/tertiary/ITEM001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemId").value("ITEM001"))
                .andExpect(jsonPath("$.currencyCode").value("USD"));
    }

    @Test
    void getItemById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        when(itemRepository.findByItemName("NON_EXISTING")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tertiary/NON_EXISTING"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createItem_WithValidData_ShouldCreateItem() throws Exception {
        when(overrideRepository.findById(1L)).thenReturn(Optional.of(testOverride));
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);

        mockMvc.perform(post("/api/tertiary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemId").value("ITEM001"));
    }

    @Test
    void createItem_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Item invalidItem = new Item();
        invalidItem.setItemId("");

        mockMvc.perform(post("/api/tertiary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidItem)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_WithExistingId_ShouldUpdateItem() throws Exception {
        Item updatedItem = new Item();
        updatedItem.setItemId("ITEM001");
        updatedItem.setCurrencyCode("EUR");
        updatedItem.setCreatedBy("UpdatedUser");
        updatedItem.setUpdatedBy("UpdatedUser");
        updatedItem.setOverride(testOverride);

        when(itemRepository.findByItemName("ITEM001")).thenReturn(Optional.of(testItem));
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        mockMvc.perform(put("/api/tertiary/ITEM001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currencyCode").value("EUR"));
    }

    @Test
    void updateItem_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        Item updatedItem = new Item();
        updatedItem.setItemId("NON_EXISTING");
        updatedItem.setCurrencyCode("EUR");

        when(itemRepository.findByItemName("NON_EXISTING")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tertiary/NON_EXISTING")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteItem_WithExistingId_ShouldDeleteItem() throws Exception {
        when(itemRepository.findByItemName("ITEM001")).thenReturn(Optional.of(testItem));
        doNothing().when(itemRepository).delete(testItem);

        mockMvc.perform(delete("/api/tertiary/ITEM001"))
                .andExpect(status().isNoContent());

        verify(itemRepository).delete(testItem);
    }

    @Test
    void deleteItem_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        when(itemRepository.findByItemName("NON_EXISTING")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/tertiary/NON_EXISTING"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createItem_WithDuplicateItemId_ShouldReturnConflict() throws Exception {
        when(itemRepository.findByItemName("ITEM001")).thenReturn(Optional.of(testItem));

        mockMvc.perform(post("/api/tertiary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isConflict());
    }
}
