package com.portfolio.portfolio_service.repository;

import com.portfolio.portfolio_service.entity.HoldingSnapshot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoldingSnapshotRepository
        extends JpaRepository<HoldingSnapshot, Long> {

    List<HoldingSnapshot> findByHoldingId(
            Long holdingId
    );
}