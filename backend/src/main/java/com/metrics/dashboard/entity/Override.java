package com.metrics.dashboard.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "overrides")
public class Override {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;
    
    @Column(name = "override_value")
    private Double overrideValue;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @OneToMany(mappedBy = "override", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detail> details;
    
    public Override() {
        this.createdDate = LocalDateTime.now();
    }
    
    public Override(Plan plan, Double overrideValue) {
        this();
        this.plan = plan;
        this.overrideValue = overrideValue;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }
    
    public Double getOverrideValue() { return overrideValue; }
    public void setOverrideValue(Double overrideValue) { this.overrideValue = overrideValue; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public List<Detail> getDetails() { return details; }
    public void setDetails(List<Detail> details) { this.details = details; }
}
