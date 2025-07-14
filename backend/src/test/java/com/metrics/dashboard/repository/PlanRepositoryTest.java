package com.metrics.dashboard.repository;

import com.metrics.dashboard.entity.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PlanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlanRepository planRepository;

    private Plan testPlan1;
    private Plan testPlan2;

    @BeforeEach
    void setUp() {
        testPlan1 = new Plan();
        testPlan1.setPlanName("Test Plan 1");
        testPlan1.setForDate(LocalDate.of(2024, 3, 31));
        testPlan1.setDataId(1);

        testPlan2 = new Plan();
        testPlan2.setPlanName("Test Plan 2");
        testPlan2.setForDate(LocalDate.of(2024, 6, 30));
        testPlan2.setDataId(2);

        entityManager.persistAndFlush(testPlan1);
        entityManager.persistAndFlush(testPlan2);
    }

    @Test
    void findByPlanName_WithExistingName_ShouldReturnPlan() {
        Optional<Plan> result = planRepository.findByPlanName("Test Plan 1");

        assertTrue(result.isPresent());
        assertEquals("Test Plan 1", result.get().getPlanName());
        assertEquals(LocalDate.of(2024, 3, 31), result.get().getForDate());
    }

    @Test
    void findByPlanName_WithNonExistingName_ShouldReturnEmpty() {
        Optional<Plan> result = planRepository.findByPlanName("Non Existing Plan");

        assertFalse(result.isPresent());
    }

    @Test
    void findByDataId_WithValidParameters_ShouldReturnPlans() {
        List<Plan> result = planRepository.findByDataId(1);

        assertEquals(1, result.size());
        assertEquals("Test Plan 1", result.get(0).getPlanName());
    }

    @Test
    void findByDataId_WithPageable_ShouldReturnPagedResults() {
        Page<Plan> result = planRepository.findByDataId(1, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Plan 1", result.getContent().get(0).getPlanName());
    }

    @Test
    void findByDataId_WithValidDataId_ShouldReturnPlans() {
        Page<Plan> result = planRepository.findByDataId(1, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Plan 1", result.getContent().get(0).getPlanName());
    }

    @Test
    void findByDataId_WithNonExistingDataId_ShouldReturnEmptyPage() {
        Page<Plan> result = planRepository.findByDataId(999, PageRequest.of(0, 10));

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void existsByPlanName_WithExistingName_ShouldReturnTrue() {
        boolean result = planRepository.existsByPlanName("Test Plan 1");

        assertTrue(result);
    }

    @Test
    void existsByPlanName_WithNonExistingName_ShouldReturnFalse() {
        boolean result = planRepository.existsByPlanName("Non Existing Plan");

        assertFalse(result);
    }

    @Test
    void save_WithValidPlan_ShouldPersistPlan() {
        Plan newPlan = new Plan();
        newPlan.setPlanName("New Test Plan");
        newPlan.setForDate(LocalDate.of(2024, 9, 30));
        newPlan.setDataId(3);

        Plan savedPlan = planRepository.save(newPlan);

        assertNotNull(savedPlan.getPlanId());
        assertEquals("New Test Plan", savedPlan.getPlanName());
        assertEquals(LocalDate.of(2024, 9, 30), savedPlan.getForDate());
        assertEquals(3, savedPlan.getDataId());
    }

    @Test
    void delete_WithExistingPlan_ShouldRemovePlan() {
        Long planId = testPlan1.getPlanId();
        
        planRepository.delete(testPlan1);
        
        Optional<Plan> result = planRepository.findById(planId);
        assertFalse(result.isPresent());
    }
}
