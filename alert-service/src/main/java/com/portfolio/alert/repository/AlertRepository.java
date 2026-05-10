package com.portfolio.alert.repository;

import com.portfolio.alert.entity.Alert;
import com.portfolio.alert.enums.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Find all active alerts for a specific user
    List<Alert> findByUserIdAndIsDeletedFalse(Long userId);

    // Find all active alerts watching a specific stock symbol
    List<Alert> findByStockSymbolAndStatusAndIsDeletedFalse(
            String stockSymbol, AlertStatus status);

    // Find all triggered alerts for a user (for history view)
    List<Alert> findByUserIdAndStatusAndIsDeletedFalse(
            Long userId, AlertStatus status);

    // Find alert by id and userId (ensures users can only access their own alerts)
    java.util.Optional<Alert> findByIdAndUserIdAndIsDeletedFalse(
            Long id, Long userId);
}