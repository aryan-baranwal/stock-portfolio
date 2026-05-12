package com.portfolio.alert.dto;

import com.portfolio.alert.enums.AlertCondition;
import com.portfolio.alert.enums.AlertStatus;
import com.portfolio.alert.enums.AlertType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponseDto {

    private Long id;
    private Long userId;
    private AlertType alertType;
    private String stockSymbol;
    private BigDecimal targetPrice;
    private AlertCondition condition;
    private Long portfolioId;
    private BigDecimal lossThresholdPercent;
    private AlertStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastTriggeredAt;
}