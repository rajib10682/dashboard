package com.metrics.dashboard.repository;

import com.metrics.dashboard.entity.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ConfigRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConfigRepository configRepository;

    private Config testConfig1;
    private Config testConfig2;

    @BeforeEach
    void setUp() {
        testConfig1 = new Config();
        testConfig1.setRedThreshold(80.0);
        testConfig1.setAmberThreshold(60.0);
        testConfig1.setGreenThreshold(40.0);

        testConfig2 = new Config();
        testConfig2.setRedThreshold(90.0);
        testConfig2.setAmberThreshold(70.0);
        testConfig2.setGreenThreshold(50.0);

        entityManager.persistAndFlush(testConfig1);
        entityManager.persistAndFlush(testConfig2);
    }

    @Test
    void findById_ShouldReturnFirstConfig() {
        Optional<Config> result = configRepository.findById(testConfig1.getId());

        assertTrue(result.isPresent());
        assertEquals(80.0, result.get().getRedThreshold());
        assertEquals(60.0, result.get().getAmberThreshold());
        assertEquals(40.0, result.get().getGreenThreshold());
    }

    @Test
    void findAll_WithNoConfigs_ShouldReturnEmpty() {
        configRepository.deleteAll();
        entityManager.flush();

        Iterable<Config> result = configRepository.findAll();

        assertFalse(result.iterator().hasNext());
    }

    @Test
    void save_WithValidConfig_ShouldPersistConfig() {
        Config newConfig = new Config();
        newConfig.setRedThreshold(85.0);
        newConfig.setAmberThreshold(65.0);
        newConfig.setGreenThreshold(45.0);

        Config savedConfig = configRepository.save(newConfig);

        assertNotNull(savedConfig.getId());
        assertEquals(85.0, savedConfig.getRedThreshold());
        assertEquals(65.0, savedConfig.getAmberThreshold());
        assertEquals(45.0, savedConfig.getGreenThreshold());
    }

    @Test
    void delete_WithExistingConfig_ShouldRemoveConfig() {
        Long configId = testConfig1.getId();
        
        configRepository.delete(testConfig1);
        
        Optional<Config> result = configRepository.findById(configId);
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllConfigs() {
        Iterable<Config> result = configRepository.findAll();

        assertNotNull(result);
        assertEquals(2, ((java.util.Collection<?>) result).size());
    }
}
