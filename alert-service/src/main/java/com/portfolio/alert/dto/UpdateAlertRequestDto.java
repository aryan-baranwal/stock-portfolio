package com.portfolio.alert.dto;

import com.portfolio.alert.enums.AlertCondition;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAlertRequestDto {

    @Positive(message = "Target price must be positive")
    private BigDecimal targetPrice;

    private AlertCondition condition;

    @Positive(message = "Loss threshold must be positive")
    private BigDecimal lossThresholdPercent;
}