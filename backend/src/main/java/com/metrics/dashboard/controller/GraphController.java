package com.metrics.dashboard.controller;

import com.metrics.dashboard.dto.TrendDataPoint;
import com.metrics.dashboard.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/graph")
public class GraphController {
    
    @Autowired
    private GraphService graphService;
    
    @GetMapping("/quarterly")
    public ResponseEntity<List<TrendDataPoint>> getQuarterlyTrends() {
        List<TrendDataPoint> trends = graphService.getQuarterlyTrends();
        return ResponseEntity.ok(trends);
    }
    
    @GetMapping("/daily")
    public ResponseEntity<List<TrendDataPoint>> getDailyTrends(
            @RequestParam(required = false) Integer dataId,
            @RequestParam(defaultValue = "30") Integer days) {
        
        List<TrendDataPoint> trends = graphService.getDailyTrends(dataId, days);
        return ResponseEntity.ok(trends);
    }
}
