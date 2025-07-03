package com.metrics.dashboard.repository;

import com.metrics.dashboard.entity.Override;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OverrideRepository extends JpaRepository<Override, Long> {
}
