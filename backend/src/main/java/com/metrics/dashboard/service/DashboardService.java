package com.metrics.dashboard.service;

import com.metrics.dashboard.dto.PlanResponse;
import com.metrics.dashboard.dto.SummaryResponse;
import com.metrics.dashboard.entity.Config;
import com.metrics.dashboard.entity.Plan;
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
        
        return plans.map(plan -> new PlanResponse(
            plan.getId(),
            plan.getPlanName(),
            plan.getMedianExecutionTime(),
            plan.getAvgItems(),
            plan.getDataId(),
            plan.getCreatedDate(),
            getColorCode(plan.getMedianExecutionTime(), config)
        ));
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
        
        int totalPlans = plans.size();
        double avgMedianTime = plans.stream()
            .mapToDouble(Plan::getMedianExecutionTime)
            .average()
            .orElse(0.0);
        int totalItems = plans.stream()
            .mapToInt(Plan::getAvgItems)
            .sum();
        
        return new SummaryResponse(totalPlans, Math.round(avgMedianTime * 100.0) / 100.0, totalItems);
    }
    
    private String getColorCode(Double executionTime, Config config) {
        if (executionTime > config.getRedThreshold()) {
            return "red";
        } else if (executionTime > config.getAmberThreshold()) {
            return "amber";
        } else {
            return "green";
        }
    }
    
    private Config getConfig() {
        return configRepository.findAll().stream()
            .findFirst()
            .orElse(new Config());
    }
}
