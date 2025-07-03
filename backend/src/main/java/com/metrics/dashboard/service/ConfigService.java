package com.metrics.dashboard.service;

import com.metrics.dashboard.dto.ThresholdConfig;
import com.metrics.dashboard.entity.Config;
import com.metrics.dashboard.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfigService {
    
    @Autowired
    private ConfigRepository configRepository;
    
    public ThresholdConfig getThresholds() {
        Config config = configRepository.findAll().stream()
            .findFirst()
            .orElse(new Config());
        
        return new ThresholdConfig(
            config.getRedThreshold(),
            config.getAmberThreshold(),
            config.getGreenThreshold()
        );
    }
    
    public ThresholdConfig updateThresholds(ThresholdConfig thresholds) {
        if (thresholds.getRedThreshold() <= thresholds.getAmberThreshold() ||
            thresholds.getAmberThreshold() <= thresholds.getGreenThreshold()) {
            throw new IllegalArgumentException("Thresholds must be in descending order: Red > Amber > Green");
        }
        
        Config config = configRepository.findAll().stream()
            .findFirst()
            .orElse(new Config());
        
        config.setRedThreshold(thresholds.getRedThreshold());
        config.setAmberThreshold(thresholds.getAmberThreshold());
        config.setGreenThreshold(thresholds.getGreenThreshold());
        config.setUpdatedDate(LocalDateTime.now());
        
        configRepository.save(config);
        
        return new ThresholdConfig(
            config.getRedThreshold(),
            config.getAmberThreshold(),
            config.getGreenThreshold()
        );
    }
}
