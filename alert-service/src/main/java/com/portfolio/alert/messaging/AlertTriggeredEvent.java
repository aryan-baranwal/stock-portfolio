package com.portfolio.alert.messaging;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertTriggeredEvent {

    private Long alertId;
    private Long userId;
    private String alertType;
    private String stockSymbol;
    private BigDecimal targetPrice;
    private BigDecimal currentPrice;
    private BigDecimal portfolioLossPercent;
    private LocalDateTime triggeredAt;
}