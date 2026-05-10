package com.portfolio.report.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class HoldingReportDto {
    private Long id;
    private String stockSymbol;
    private BigDecimal quantity;
    private BigDecimal buyPrice;
    private BigDecimal currentPrice;
    private BigDecimal currentValue;
    private BigDecimal gainLoss;
    private BigDecimal gainLossPercent;
}