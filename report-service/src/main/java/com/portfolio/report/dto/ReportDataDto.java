package com.portfolio.report.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ReportDataDto {
    private Long portfolioId;
    private String portfolioName;
    private BigDecimal totalInvestedValue;
    private BigDecimal totalCurrentValue;
    private BigDecimal totalGainLoss;
    private BigDecimal totalGainLossPercent;
    private List<HoldingReportDto> holdings;
}