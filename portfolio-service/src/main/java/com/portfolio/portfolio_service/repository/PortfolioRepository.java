package com.portfolio.portfolio_service.repository;

import com.portfolio.portfolio_service.entity.Portfolio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository
        extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByUserIdAndIsActiveTrue(
            Long userId
    );
}