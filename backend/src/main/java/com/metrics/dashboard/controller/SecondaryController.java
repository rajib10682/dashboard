package com.metrics.dashboard.controller;

import com.metrics.dashboard.entity.Override;
import com.metrics.dashboard.repository.OverrideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/secondary")
@CrossOrigin(origins = "*")
public class SecondaryController {
    
    @Autowired
    private OverrideRepository overrideRepository;
    
    @GetMapping
    public ResponseEntity<List<Override>> getAllOverrides() {
        List<Override> overrides = overrideRepository.findAll();
        return ResponseEntity.ok(overrides);
    }
    
    @GetMapping("/{overrideId}")
    public ResponseEntity<Override> getOverride(@PathVariable Long overrideId) {
        Optional<Override> override = overrideRepository.findById(overrideId);
        return override.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{overrideId}")
    public ResponseEntity<Void> deleteOverride(@PathVariable Long overrideId) {
        Optional<Override> override = overrideRepository.findById(overrideId);
        if (override.isPresent()) {
            overrideRepository.deleteById(overrideId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Override> createOverride(@Valid @RequestBody Override override) {
        try {
            override.setOverrideId(null);
            Override savedOverride = overrideRepository.save(override);
            return ResponseEntity.ok(savedOverride);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{overrideId}")
    @Transactional
    public ResponseEntity<Override> updateOverride(@PathVariable Long overrideId, @RequestBody Override override) {
        Optional<Override> existingOverride = overrideRepository.findById(overrideId);
        if (existingOverride.isPresent()) {
            Override existing = existingOverride.get();
            existing.setOverrideName(override.getOverrideName());
            existing.setTotalExecutionTime(override.getTotalExecutionTime());
            existing.setOnHoldTime(override.getOnHoldTime());
            existing.setCoreExecutionTime(override.getCoreExecutionTime());
            existing.setRequestType(override.getRequestType());
            try {
                Override savedOverride = overrideRepository.save(existing);
                return ResponseEntity.ok(savedOverride);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
