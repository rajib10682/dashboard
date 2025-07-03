package com.metrics.dashboard.controller;

import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/master")
@CrossOrigin(origins = "*")
public class MasterController {
    
    @Autowired
    private PlanRepository planRepository;
    
    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long planId) {
        Optional<Plan> plan = planRepository.findById(planId);
        if (plan.isPresent()) {
            planRepository.deleteById(planId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
