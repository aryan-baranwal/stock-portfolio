package com.portfolio.notification.dto.event;

import lombok.Data;

@Data
public class DailySummaryEvent {
    private Long userId;
    private String userEmail;
    private String reportId;
    private String generatedAt;
}