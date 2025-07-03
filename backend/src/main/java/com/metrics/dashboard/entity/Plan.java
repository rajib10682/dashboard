package com.metrics.dashboard.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "plans")
public class Plan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "plan_name", nullable = false)
    private String planName;
    
    @Column(name = "median_execution_time")
    private Double medianExecutionTime;
    
    @Column(name = "avg_items")
    private Integer avgItems;
    
    @Column(name = "data_id")
    private Integer dataId;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Override> overrides;
    
    public Plan() {
        this.createdDate = LocalDateTime.now();
    }
    
    public Plan(String planName, Double medianExecutionTime, Integer avgItems, Integer dataId) {
        this();
        this.planName = planName;
        this.medianExecutionTime = medianExecutionTime;
        this.avgItems = avgItems;
        this.dataId = dataId;
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
    
    public List<Override> getOverrides() { return overrides; }
    public void setOverrides(List<Override> overrides) { this.overrides = overrides; }
}
