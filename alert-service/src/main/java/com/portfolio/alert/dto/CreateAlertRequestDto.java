package com.portfolio.alert.dto;

import com.portfolio.alert.enums.AlertCondition;
import com.portfolio.alert.enums.AlertType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAlertRequestDto {

    @NotNull(message = "Alert type is required")
    private AlertType alertType;

    // Required for PRICE_THRESHOLD alerts
    private String stockSymbol;

    @Positive(message = "Target price must be positive")
    private BigDecimal targetPrice;

    private AlertCondition condition;

    // Required for PORTFOLIO_LOSS_PERCENT alerts
    private Long portfolioId;

    @Positive(message = "Loss threshold must be positive")
    private BigDecimal lossThresholdPercent;
}