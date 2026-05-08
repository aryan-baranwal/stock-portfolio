package com.portfolio.notification.repository;

import com.portfolio.notification.entity.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    Page<NotificationLog> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    long countByUserIdAndIsReadFalse(Long userId);

    @Query("SELECT n FROM NotificationLog n WHERE n.status = 'FAILED'")
    Page<NotificationLog> findFailedNotifications(Pageable pageable);
}