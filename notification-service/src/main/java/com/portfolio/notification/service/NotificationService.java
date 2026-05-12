package com.portfolio.notification.service;

import com.portfolio.notification.dto.NotificationLogDto;
import com.portfolio.notification.dto.event.AlertTriggeredEvent;
import com.portfolio.notification.dto.event.DailySummaryEvent;
import com.portfolio.notification.dto.event.UserRegisteredEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    void handleUserRegistered(UserRegisteredEvent event);
    void handleAlertTriggered(AlertTriggeredEvent event);
    void handleDailySummary(DailySummaryEvent event);
    Page<NotificationLogDto> getNotifications(Long userId, Pageable pageable);
    NotificationLogDto getNotificationById(Long id);
    void markAsRead(Long id);
    long getUnreadCount(Long userId);
}