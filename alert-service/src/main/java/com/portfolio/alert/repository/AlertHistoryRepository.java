package com.portfolio.alert.repository;

import com.portfolio.alert.entity.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {

    // Get all history records for a specific alert
    List<AlertHistory> findByAlertIdOrderByTriggeredAtDesc(Long alertId);

    // Get all history records for a specific user
    List<AlertHistory> findByUserIdOrderByTriggeredAtDesc(Long userId);
}