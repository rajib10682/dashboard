package com.metrics.dashboard.dto;

import java.time.LocalDate;

public class PlanResponse {
    private Long planId;
    private String planName;
    private LocalDate forDate;
    private Integer dataId;
    private Double avgCoreExecutionTime;
    private String colorCode;
    
    public PlanResponse() {}
    
    public PlanResponse(Long planId, String planName, LocalDate forDate, Integer dataId, Double avgCoreExecutionTime, String colorCode) {
        this.planId = planId;
        this.planName = planName;
        this.forDate = forDate;
        this.dataId = dataId;
        this.avgCoreExecutionTime = avgCoreExecutionTime;
        this.colorCode = colorCode;
    }
    
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    
    public LocalDate getForDate() { return forDate; }
    public void setForDate(LocalDate forDate) { this.forDate = forDate; }
    
    public Integer getDataId() { return dataId; }
    public void setDataId(Integer dataId) { this.dataId = dataId; }
    
    public Double getAvgCoreExecutionTime() { return avgCoreExecutionTime; }
    public void setAvgCoreExecutionTime(Double avgCoreExecutionTime) { this.avgCoreExecutionTime = avgCoreExecutionTime; }
    
    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }
}
