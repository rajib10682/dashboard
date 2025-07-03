package com.metrics.dashboard.service;

import com.metrics.dashboard.dto.TrendDataPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GraphService {
    
    private final Random random = new Random();
    
    public List<TrendDataPoint> getQuarterlyTrends() {
        List<TrendDataPoint> trends = new ArrayList<>();
        LocalDateTime baseDate = LocalDateTime.now();
        
        for (int i = 0; i < 8; i++) {
            LocalDateTime quarterDate = baseDate.minusMonths(3L * i);
            int quarter = ((quarterDate.getMonthValue() - 1) / 3) + 1;
            String period = "Q" + quarter + " " + quarterDate.getYear();
            double medianValue = 30 + random.nextDouble() * 90;
            
            trends.add(new TrendDataPoint(period, Math.round(medianValue * 100.0) / 100.0, quarterDate));
        }
        
        trends.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        return trends;
    }
    
    public List<TrendDataPoint> getDailyTrends(Integer dataId, Integer days) {
        List<TrendDataPoint> trends = new ArrayList<>();
        LocalDateTime baseDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (int i = 0; i < days; i++) {
            LocalDateTime dayDate = baseDate.minusDays(i);
            String period = dayDate.format(formatter);
            double medianValue = 20 + random.nextDouble() * 130;
            
            trends.add(new TrendDataPoint(period, Math.round(medianValue * 100.0) / 100.0, dayDate));
        }
        
        trends.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        return trends;
    }
}
