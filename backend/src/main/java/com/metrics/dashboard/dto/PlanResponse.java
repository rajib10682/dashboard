package com.metrics.dashboard.dto;

import java.time.LocalDateTime;

public class PlanResponse {
    private Long id;
    private String planName;
    private Double medianExecutionTime;
    private Integer avgItems;
    private Integer dataId;
    private LocalDateTime createdDate;
    private String colorCode;
    
    public PlanResponse() {}
    
    public PlanResponse(Long id, String planName, Double medianExecutionTime, 
                       Integer avgItems, Integer dataId, LocalDateTime createdDate, String colorCode) {
        this.id = id;
        this.planName = planName;
        this.medianExecutionTime = medianExecutionTime;
        this.avgItems = avgItems;
        this.dataId = dataId;
        this.createdDate = createdDate;
        this.colorCode = colorCode;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    
    public Double getMedianExecutionTime() { return medianExecutionTime; }
    public void setMedianExecutionTime(Double medianExecutionTime) { this.medianExecutionTime = medianExecutionTime; }
    
    public Integer getAvgItems() { return avgItems; }
    public void setAvgItems(Integer avgItems) { this.avgItems = avgItems; }
    
    public Integer getDataId() { return dataId; }
    public void setDataId(Integer dataId) { this.dataId = dataId; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }
}
