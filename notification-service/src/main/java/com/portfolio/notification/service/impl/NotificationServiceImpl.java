package com.portfolio.notification.service.impl;

import com.portfolio.notification.dto.NotificationLogDto;
import com.portfolio.notification.dto.event.AlertTriggeredEvent;
import com.portfolio.notification.dto.event.DailySummaryEvent;
import com.portfolio.notification.dto.event.UserRegisteredEvent;
import com.portfolio.notification.entity.NotificationLog;
import com.portfolio.notification.enums.NotificationStatus;
import com.portfolio.notification.enums.NotificationType;
import com.portfolio.notification.mapper.NotificationMapper;
import com.portfolio.notification.repository.NotificationLogRepository;
import com.portfolio.notification.service.EmailService;
import com.portfolio.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationLogRepository notificationLogRepository;
    private final EmailService emailService;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void handleUserRegistered(UserRegisteredEvent event) {
        String subject = "Welcome to Stock Portfolio App!";
        String body = "Hi " + event.getFullName() + ",\n\n"
                + "Welcome! Your account has been successfully created.\n"
                + "Start tracking your stock portfolio today.\n\n"
                + "Best regards,\nStock Portfolio Team";

        sendAndLog(event.getUserId(), event.getEmail(), subject, body, NotificationType.WELCOME_EMAIL);
    }

    @Override
    @Transactional
    public void handleAlertTriggered(AlertTriggeredEvent event) {
        String subject = "Price Alert Triggered: " + event.getStockSymbol();
        String body = "Hi,\n\n"
                + "Your price alert for " + event.getStockSymbol() + " has been triggered.\n"
                + "Alert Type: " + event.getAlertType() + "\n"
                + "Condition: " + event.getCondition() + "\n"
                + "Target Price: " + event.getTargetPrice() + "\n"
                + "Current Price: " + event.getCurrentPrice() + "\n"
                + "Triggered At: " + event.getTriggeredAt() + "\n\n"
                + "Best regards,\nStock Portfolio Team";

        sendAndLog(event.getUserId(), event.getUserEmail(), subject, body, NotificationType.ALERT_TRIGGERED);
    }

    @Override
    @Transactional
    public void handleDailySummary(DailySummaryEvent event) {
        String subject = "Your Daily Portfolio Summary";
        String body = "Hi,\n\n"
                + "Your daily portfolio summary report has been generated.\n"
                + "Report ID: " + event.getReportId() + "\n"
                + "Generated At: " + event.getGeneratedAt() + "\n\n"
                + "Please log in to view and download your report.\n\n"
                + "Best regards,\nStock Portfolio Team";

        sendAndLog(event.getUserId(), event.getUserEmail(), subject, body, NotificationType.DAILY_SUMMARY);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationLogDto> getNotifications(Long userId, Pageable pageable) {
        return notificationLogRepository
                .findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(notificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationLogDto getNotificationById(Long id) {
        NotificationLog entity = notificationLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        return notificationMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        NotificationLog entity = notificationLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        entity.setIsRead(true);
        notificationLogRepository.save(entity);
        log.info("Notification marked as read: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationLogRepository.countByUserIdAndIsReadFalse(userId);
    }

    private void sendAndLog(Long userId, String email, String subject,
                            String body, NotificationType type) {

        NotificationLog logEntry = NotificationLog.builder()
                .userId(userId)
                .recipientEmail(email)
                .subject(subject)
                .body(body)
                .type(type)
                .status(NotificationStatus.PENDING)
                .build();

        try {
            emailService.sendEmail(email, subject, body);
            logEntry.setStatus(NotificationStatus.SENT);
            logEntry.setSentAt(LocalDateTime.now());
            log.info("Notification SENT to: {} | Type: {}", email, type);
        } catch (Exception e) {
            logEntry.setStatus(NotificationStatus.FAILED);
            logEntry.setErrorMessage(e.getMessage());
            log.error("Notification FAILED to: {} | Error: {}", email, e.getMessage());
            throw e;
        } finally {
            notificationLogRepository.save(logEntry);
        }
    }
}