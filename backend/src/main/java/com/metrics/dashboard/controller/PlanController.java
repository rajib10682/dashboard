package com.metrics.dashboard.controller;

import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    
    @Autowired
    private PlanRepository planRepository;
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        Optional<Plan> plan = planRepository.findById(id);
        if (plan.isPresent()) {
            planRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
