package com.metrics.dashboard.repository;

import com.metrics.dashboard.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    
    Page<Plan> findByDataId(Integer dataId, Pageable pageable);
    
    List<Plan> findByDataId(Integer dataId);
    
    @Query("SELECT AVG(p.medianExecutionTime) FROM Plan p WHERE p.dataId = :dataId")
    Double findAvgMedianTimeByDataId(@Param("dataId") Integer dataId);
    
    @Query("SELECT SUM(p.avgItems) FROM Plan p WHERE p.dataId = :dataId")
    Integer findTotalItemsByDataId(@Param("dataId") Integer dataId);
    
    @Query("SELECT AVG(p.medianExecutionTime) FROM Plan p")
    Double findAvgMedianTime();
    
    @Query("SELECT SUM(p.avgItems) FROM Plan p")
    Integer findTotalItems();
}
