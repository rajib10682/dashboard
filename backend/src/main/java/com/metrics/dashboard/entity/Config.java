package com.metrics.dashboard.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "config")
public class Config {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "red_threshold")
    private Double redThreshold = 100.0;
    
    @Column(name = "amber_threshold")
    private Double amberThreshold = 50.0;
    
    @Column(name = "green_threshold")
    private Double greenThreshold = 25.0;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    public Config() {
        this.updatedDate = LocalDateTime.now();
    }
    
    public Config(Double redThreshold, Double amberThreshold, Double greenThreshold) {
        this();
        this.redThreshold = redThreshold;
        this.amberThreshold = amberThreshold;
        this.greenThreshold = greenThreshold;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Double getRedThreshold() { return redThreshold; }
    public void setRedThreshold(Double redThreshold) { this.redThreshold = redThreshold; }
    
    public Double getAmberThreshold() { return amberThreshold; }
    public void setAmberThreshold(Double amberThreshold) { this.amberThreshold = amberThreshold; }
    
    public Double getGreenThreshold() { return greenThreshold; }
    public void setGreenThreshold(Double greenThreshold) { this.greenThreshold = greenThreshold; }
    
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}
