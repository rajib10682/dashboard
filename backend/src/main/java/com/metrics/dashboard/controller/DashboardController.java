package com.metrics.dashboard.controller;

import com.metrics.dashboard.dto.PlanResponse;
import com.metrics.dashboard.dto.SummaryResponse;
import com.metrics.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/data")
    public ResponseEntity<Page<PlanResponse>> getDashboardData(
            @RequestParam(required = false) Integer dataId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PlanResponse> data = dashboardService.getDashboardData(dataId, pageable);
        
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getDashboardSummary(
            @RequestParam(required = false) Integer dataId) {
        
        SummaryResponse summary = dashboardService.getDashboardSummary(dataId);
        return ResponseEntity.ok(summary);
    }
}
