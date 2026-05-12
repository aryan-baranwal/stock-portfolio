package com.portfolio.portfolio_service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PortfolioSummaryDto
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long portfolioId;

    private String name;

    private BigDecimal totalInvestedValue;

    private BigDecimal totalCurrentValue;

    private BigDecimal totalGainLoss;

    private BigDecimal totalGainLossPercent;

    private Integer holdingCount;

    private String currency;

    private LocalDateTime asOfTime;

    public PortfolioSummaryDto() {
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalInvestedValue() {
        return totalInvestedValue;
    }

    public void setTotalInvestedValue(
            BigDecimal totalInvestedValue
    ) {

        this.totalInvestedValue = totalInvestedValue;
    }

    public BigDecimal getTotalCurrentValue() {
        return totalCurrentValue;
    }

    public void setTotalCurrentValue(
            BigDecimal totalCurrentValue
    ) {

        this.totalCurrentValue = totalCurrentValue;
    }

    public BigDecimal getTotalGainLoss() {
        return totalGainLoss;
    }

    public void setTotalGainLoss(
            BigDecimal totalGainLoss
    ) {

        this.totalGainLoss = totalGainLoss;
    }

    public BigDecimal getTotalGainLossPercent() {
        return totalGainLossPercent;
    }

    public void setTotalGainLossPercent(
            BigDecimal totalGainLossPercent
    ) {

        this.totalGainLossPercent = totalGainLossPercent;
    }

    public Integer getHoldingCount() {
        return holdingCount;
    }

    public void setHoldingCount(Integer holdingCount) {
        this.holdingCount = holdingCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getAsOfTime() {
        return asOfTime;
    }

    public void setAsOfTime(
            LocalDateTime asOfTime
    ) {

        this.asOfTime = asOfTime;
    }
}