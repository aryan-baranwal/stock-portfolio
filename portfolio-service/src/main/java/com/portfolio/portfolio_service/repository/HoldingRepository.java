package com.portfolio.portfolio_service.repository;

import com.portfolio.portfolio_service.entity.Holding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoldingRepository
        extends JpaRepository<Holding, Long> {

    @Query("""
            SELECT h
            FROM Holding h
            WHERE h.portfolio.id = :pid
            AND h.isDeleted = false
            """)
    List<Holding> findActiveHoldingsByPortfolio(
            @Param("pid") Long portfolioId
    );

    @Query("""
            SELECT DISTINCT h.stockSymbol
            FROM Holding h
            WHERE h.isDeleted = false
            """)
    List<String> findAllActiveSymbols();

    @Query(
            value = """
                    SELECT *
                    FROM holdings
                    WHERE portfolio_id = ?1
                    AND is_deleted = false
                    ORDER BY quantity DESC
                    LIMIT ?2
                    """,
            nativeQuery = true
    )
    List<Holding> findTopGainersByPortfolio(
            Long portfolioId,
            int limit
    );
}