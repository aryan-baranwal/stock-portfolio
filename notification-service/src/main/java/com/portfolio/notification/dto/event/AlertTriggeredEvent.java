package com.portfolio.notification.dto.event;

import lombok.Data;

@Data
public class AlertTriggeredEvent {
    private Long alertId;
    private Long userId;
    private String userEmail;
    private String stockSymbol;
    private Double targetPrice;
    private Double currentPrice;
    private String alertType;
    private String condition;
    private String triggeredAt;
}