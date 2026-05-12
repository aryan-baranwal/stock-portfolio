package com.portfolio.portfolio_service.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PortfolioSummaryReadyEvent
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long portfolioId;

    private String portfolioName;

    private BigDecimal totalCurrentValue;

    private BigDecimal totalGainLoss;

    private LocalDateTime generatedAt;

    public PortfolioSummaryReadyEvent() {
    }

    public PortfolioSummaryReadyEvent(
            Long portfolioId,
            String portfolioName,
            BigDecimal totalCurrentValue,
            BigDecimal totalGainLoss,
            LocalDateTime generatedAt
    ) {

        this.portfolioId = portfolioId;
        this.portfolioName = portfolioName;
        this.totalCurrentValue = totalCurrentValue;
        this.totalGainLoss = totalGainLoss;
        this.generatedAt = generatedAt;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
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

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(
            LocalDateTime generatedAt
    ) {

        this.generatedAt = generatedAt;
    }
}