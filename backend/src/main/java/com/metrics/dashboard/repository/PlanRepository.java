package com.metrics.dashboard.repository;

import com.metrics.dashboard.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    
    Page<Plan> findByDataId(Integer dataId, Pageable pageable);
    
    List<Plan> findByDataId(Integer dataId);
    
    boolean existsByPlanName(String planName);
}
