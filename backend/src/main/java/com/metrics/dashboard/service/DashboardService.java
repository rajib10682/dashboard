package com.metrics.dashboard.service;

import com.metrics.dashboard.dto.PlanResponse;
import com.metrics.dashboard.dto.SummaryResponse;
import com.metrics.dashboard.entity.Config;
import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.entity.Override;
import com.metrics.dashboard.repository.ConfigRepository;
import com.metrics.dashboard.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    
    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private ConfigRepository configRepository;
    
    public Page<PlanResponse> getDashboardData(Integer dataId, Pageable pageable) {
        Page<Plan> plans;
        if (dataId != null) {
            plans = planRepository.findByDataId(dataId, pageable);
        } else {
            plans = planRepository.findAll(pageable);
        }
        
        Config config = getConfig();
        
        return plans.map(plan -> {
            Double avgCoreExecutionTime = calculateAvgCoreExecutionTime(plan);
            String colorCode = getColorCode(avgCoreExecutionTime, config);
            return new PlanResponse(
                plan.getPlanId(),
                plan.getPlanName(),
                plan.getForDate(),
                plan.getDataId(),
                avgCoreExecutionTime,
                colorCode
            );
        });
    }
    
    public SummaryResponse getDashboardSummary(Integer dataId) {
        List<Plan> plans;
        if (dataId != null) {
            plans = planRepository.findByDataId(dataId);
        } else {
            plans = planRepository.findAll();
        }
        
        if (plans.isEmpty()) {
            return new SummaryResponse(0, 0.0, 0);
        }
        
        double totalCoreExecutionTime = plans.stream()
            .mapToDouble(this::calculateAvgCoreExecutionTime)
            .sum();
        
        double avgCoreExecutionTime = totalCoreExecutionTime / plans.size();
        
        int totalOverrides = plans.stream()
            .mapToInt(plan -> plan.getOverrides() != null ? plan.getOverrides().size() : 0)
            .sum();
        
        return new SummaryResponse(plans.size(), avgCoreExecutionTime, totalOverrides);
    }
    
    private Double calculateAvgCoreExecutionTime(Plan plan) {
        if (plan.getOverrides() == null || plan.getOverrides().isEmpty()) {
            return 0.0;
        }
        
        return plan.getOverrides().stream()
            .mapToDouble(Override::getCoreExecutionTime)
            .average()
            .orElse(0.0);
    }
    
    private Config getConfig() {
        return configRepository.findAll().stream().findFirst()
            .orElse(new Config(100.0, 50.0, 25.0));
    }
    
    private String getColorCode(Double executionTime, Config config) {
        if (executionTime == null) return "green";
        
        if (executionTime >= config.getRedThreshold()) {
            return "red";
        } else if (executionTime >= config.getAmberThreshold()) {
            return "amber";
        } else {
            return "green";
        }
    }
}
