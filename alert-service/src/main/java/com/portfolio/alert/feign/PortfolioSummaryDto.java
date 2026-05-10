package com.portfolio.alert.feign;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioSummaryDto {

    private Long portfolioId;
    private String name;
    private BigDecimal totalInvestedValue;
    private BigDecimal totalCurrentValue;
    private BigDecimal totalGainLoss;
    private BigDecimal totalGainLossPercent;
    private Integer holdingCount;
    private String currency;

    // Manual getter to ensure compilation
    public BigDecimal getTotalGainLossPercent() {
        return this.totalGainLossPercent;
    }
}