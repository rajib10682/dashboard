package com.metrics.dashboard.dto;

public class SummaryResponse {
    private Integer totalPlans;
    private Double avgMedianTime;
    private Integer totalItems;
    
    public SummaryResponse() {}
    
    public SummaryResponse(Integer totalPlans, Double avgMedianTime, Integer totalItems) {
        this.totalPlans = totalPlans;
        this.avgMedianTime = avgMedianTime;
        this.totalItems = totalItems;
    }
    
    public Integer getTotalPlans() { return totalPlans; }
    public void setTotalPlans(Integer totalPlans) { this.totalPlans = totalPlans; }
    
    public Double getAvgMedianTime() { return avgMedianTime; }
    public void setAvgMedianTime(Double avgMedianTime) { this.avgMedianTime = avgMedianTime; }
    
    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }
}
