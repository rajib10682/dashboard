package com.metrics.dashboard.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ThresholdConfig {
    
    @NotNull
    @Positive
    private Double redThreshold;
    
    @NotNull
    @Positive
    private Double amberThreshold;
    
    @NotNull
    @Positive
    private Double greenThreshold;
    
    public ThresholdConfig() {}
    
    public ThresholdConfig(Double redThreshold, Double amberThreshold, Double greenThreshold) {
        this.redThreshold = redThreshold;
        this.amberThreshold = amberThreshold;
        this.greenThreshold = greenThreshold;
    }
    
    public Double getRedThreshold() { return redThreshold; }
    public void setRedThreshold(Double redThreshold) { this.redThreshold = redThreshold; }
    
    public Double getAmberThreshold() { return amberThreshold; }
    public void setAmberThreshold(Double amberThreshold) { this.amberThreshold = amberThreshold; }
    
    public Double getGreenThreshold() { return greenThreshold; }
    public void setGreenThreshold(Double greenThreshold) { this.greenThreshold = greenThreshold; }
}
