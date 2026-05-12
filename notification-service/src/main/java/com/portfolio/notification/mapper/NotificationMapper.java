package com.portfolio.notification.mapper;

import com.portfolio.notification.dto.NotificationLogDto;
import com.portfolio.notification.entity.NotificationLog;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationLogDto toDto(NotificationLog entity) {
        return NotificationLogDto.builder()
                .id(entity.getId())
                .recipientEmail(entity.getRecipientEmail())
                .subject(entity.getSubject())
                .type(entity.getType())
                .status(entity.getStatus())
                .isRead(entity.getIsRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}