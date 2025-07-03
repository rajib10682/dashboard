package com.metrics.dashboard.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "details")
public class Detail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "override_id", nullable = false)
    private Override override;
    
    @Column(name = "detail_data")
    private String detailData;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    public Detail() {
        this.createdDate = LocalDateTime.now();
    }
    
    public Detail(Override override, String detailData) {
        this();
        this.override = override;
        this.detailData = detailData;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Override getOverride() { return override; }
    public void setOverride(Override override) { this.override = override; }
    
    public String getDetailData() { return detailData; }
    public void setDetailData(String detailData) { this.detailData = detailData; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
