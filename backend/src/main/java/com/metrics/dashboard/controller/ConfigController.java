package com.metrics.dashboard.controller;

import com.metrics.dashboard.dto.ThresholdConfig;
import com.metrics.dashboard.service.ConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigController {
    
    @Autowired
    private ConfigService configService;
    
    @GetMapping("/thresholds")
    public ResponseEntity<ThresholdConfig> getThresholds() {
        ThresholdConfig thresholds = configService.getThresholds();
        return ResponseEntity.ok(thresholds);
    }
    
    @PutMapping("/thresholds")
    public ResponseEntity<ThresholdConfig> updateThresholds(@Valid @RequestBody ThresholdConfig thresholds) {
        try {
            ThresholdConfig updated = configService.updateThresholds(thresholds);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
