package com.portfolio.price_fetcher_service.repository;

import com.portfolio.price_fetcher_service.entity.PriceHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceHistoryRepository
        extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findBySymbol(String symbol);
}