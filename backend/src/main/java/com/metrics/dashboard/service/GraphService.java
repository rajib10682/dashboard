package com.metrics.dashboard.service;

import com.metrics.dashboard.dto.TrendDataPoint;
import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.entity.Override;
import com.metrics.dashboard.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GraphService {
    
    @Autowired
    private PlanRepository planRepository;
    
    public List<TrendDataPoint> getQuarterlyTrends(Integer dataId) {
        List<Plan> plans;
        if (dataId != null) {
            plans = planRepository.findByDataId(dataId);
        } else {
            plans = planRepository.findAll();
        }
        
        Map<String, Double> quarterlyData = plans.stream()
            .collect(Collectors.groupingBy(
                plan -> getQuarter(plan.getForDate()),
                Collectors.averagingDouble(this::calculateAvgCoreExecutionTime)
            ));
        
        return quarterlyData.entrySet().stream()
            .map(entry -> new TrendDataPoint(entry.getKey(), entry.getValue(), null))
            .collect(Collectors.toList());
    }
    
    public List<TrendDataPoint> getDailyTrends(Integer dataId) {
        List<Plan> plans;
        if (dataId != null) {
            plans = planRepository.findByDataId(dataId);
        } else {
            plans = planRepository.findAll();
        }
        
        Map<String, Double> dailyData = plans.stream()
            .collect(Collectors.groupingBy(
                plan -> plan.getForDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                Collectors.averagingDouble(this::calculateAvgCoreExecutionTime)
            ));
        
        return dailyData.entrySet().stream()
            .map(entry -> new TrendDataPoint(entry.getKey(), entry.getValue(), null))
            .collect(Collectors.toList());
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
    
    private String getQuarter(LocalDate date) {
        int month = date.getMonthValue();
        int year = date.getYear();
        
        if (month <= 3) {
            return "Q1 " + year;
        } else if (month <= 6) {
            return "Q2 " + year;
        } else if (month <= 9) {
            return "Q3 " + year;
        } else {
            return "Q4 " + year;
        }
    }
}
