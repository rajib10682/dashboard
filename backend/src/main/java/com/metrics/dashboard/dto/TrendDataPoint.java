package com.metrics.dashboard.dto;

import java.time.LocalDateTime;

public class TrendDataPoint {
    private String period;
    private Double medianValue;
    private LocalDateTime date;
    
    public TrendDataPoint() {}
    
    public TrendDataPoint(String period, Double medianValue, LocalDateTime date) {
        this.period = period;
        this.medianValue = medianValue;
        this.date = date;
    }
    
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    
    public Double getMedianValue() { return medianValue; }
    public void setMedianValue(Double medianValue) { this.medianValue = medianValue; }
    
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
