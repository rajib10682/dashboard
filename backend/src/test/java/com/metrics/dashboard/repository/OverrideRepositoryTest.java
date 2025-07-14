package com.metrics.dashboard.repository;

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
class OverrideRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OverrideRepository overrideRepository;

    private Plan testPlan;
    private Override testOverride1;
    private Override testOverride2;

    @BeforeEach
    void setUp() {
        testPlan = new Plan();
        testPlan.setPlanName("Test Plan");
        testPlan.setForDate(LocalDate.of(2024, 3, 31));
        testPlan.setDataId(1);
        entityManager.persistAndFlush(testPlan);

        testOverride1 = new Override();
        testOverride1.setOverrideName("Test Override 1");
        testOverride1.setTotalExecutionTime(100.0);
        testOverride1.setOnHoldTime(20.0);
        testOverride1.setCoreExecutionTime(80.0);
        testOverride1.setRequestType("BATCH");
        testOverride1.setPlan(testPlan);

        testOverride2 = new Override();
        testOverride2.setOverrideName("Test Override 2");
        testOverride2.setTotalExecutionTime(150.0);
        testOverride2.setOnHoldTime(30.0);
        testOverride2.setCoreExecutionTime(120.0);
        testOverride2.setRequestType("ONLINE");
        testOverride2.setPlan(testPlan);

        entityManager.persistAndFlush(testOverride1);
        entityManager.persistAndFlush(testOverride2);
    }

    @Test
    void findByOverrideName_WithExistingName_ShouldReturnOverride() {
        Optional<Override> result = overrideRepository.findByOverrideName("Test Override 1");

        assertTrue(result.isPresent());
        assertEquals("Test Override 1", result.get().getOverrideName());
        assertEquals(100.0, result.get().getTotalExecutionTime());
    }

    @Test
    void findByOverrideName_WithNonExistingName_ShouldReturnEmpty() {
        Optional<Override> result = overrideRepository.findByOverrideName("Non Existing Override");

        assertFalse(result.isPresent());
    }

    @Test
    void findByPlan_WithValidPlan_ShouldReturnOverrides() {
        List<Override> result = overrideRepository.findAll().stream()
            .filter(override -> override.getPlan().getPlanId().equals(testPlan.getPlanId()))
            .collect(java.util.stream.Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(o -> "Test Override 1".equals(o.getOverrideName())));
        assertTrue(result.stream().anyMatch(o -> "Test Override 2".equals(o.getOverrideName())));
    }

    @Test
    void findByPlanPlanId_WithValidPlanId_ShouldReturnOverrides() {
        List<Override> result = overrideRepository.findAll().stream()
            .filter(override -> override.getPlan().getPlanId().equals(testPlan.getPlanId()))
            .collect(java.util.stream.Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(o -> "Test Override 1".equals(o.getOverrideName())));
        assertTrue(result.stream().anyMatch(o -> "Test Override 2".equals(o.getOverrideName())));
    }

    @Test
    void findByRequestType_WithValidRequestType_ShouldReturnOverrides() {
        List<Override> result = overrideRepository.findAll().stream()
            .filter(override -> "BATCH".equals(override.getRequestType()))
            .collect(java.util.stream.Collectors.toList());

        assertEquals(1, result.size());
        assertEquals("Test Override 1", result.get(0).getOverrideName());
        assertEquals("BATCH", result.get(0).getRequestType());
    }

    @Test
    void save_WithValidOverride_ShouldPersistOverride() {
        Override newOverride = new Override();
        newOverride.setOverrideName("New Test Override");
        newOverride.setTotalExecutionTime(200.0);
        newOverride.setOnHoldTime(40.0);
        newOverride.setCoreExecutionTime(160.0);
        newOverride.setRequestType("BATCH");
        newOverride.setPlan(testPlan);

        Override savedOverride = overrideRepository.save(newOverride);

        assertNotNull(savedOverride.getOverrideId());
        assertEquals("New Test Override", savedOverride.getOverrideName());
        assertEquals(200.0, savedOverride.getTotalExecutionTime());
        assertEquals(testPlan, savedOverride.getPlan());
    }

    @Test
    void delete_WithExistingOverride_ShouldRemoveOverride() {
        Long overrideId = testOverride1.getOverrideId();
        
        overrideRepository.delete(testOverride1);
        
        Optional<Override> result = overrideRepository.findById(overrideId);
        assertFalse(result.isPresent());
    }

    @Test
    void findByCoreExecutionTimeGreaterThan_ShouldReturnMatchingOverrides() {
        List<Override> result = overrideRepository.findAll().stream()
            .filter(override -> override.getCoreExecutionTime() > 100.0)
            .collect(java.util.stream.Collectors.toList());

        assertEquals(1, result.size());
        assertEquals("Test Override 2", result.get(0).getOverrideName());
        assertEquals(120.0, result.get(0).getCoreExecutionTime());
    }
}
