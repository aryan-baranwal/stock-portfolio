package com.portfolio.notification.dto;

import com.portfolio.notification.enums.NotificationStatus;
import com.portfolio.notification.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationLogDto {
    private Long id;
    private String recipientEmail;
    private String subject;
    private NotificationType type;
    private NotificationStatus status;
    private Boolean isRead;
    private LocalDateTime createdAt;
}