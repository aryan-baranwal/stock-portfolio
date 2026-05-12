package com.portfolio.portfolio_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "holding_snapshots")
public class HoldingSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "holding_id")
    private Holding holding;

    private LocalDateTime snapshotDate;

    private BigDecimal currentPrice;

    private BigDecimal gainLoss;

    private BigDecimal gainLossPercent;

    public HoldingSnapshot() {
    }

    public Long getId() {
        return id;
    }

    public Holding getHolding() {
        return holding;
    }

    public void setHolding(Holding holding) {
        this.holding = holding;
    }

    public LocalDateTime getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(
            LocalDateTime snapshotDate
    ) {

        this.snapshotDate = snapshotDate;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(
            BigDecimal currentPrice
    ) {

        this.currentPrice = currentPrice;
    }

    public BigDecimal getGainLoss() {
        return gainLoss;
    }

    public void setGainLoss(
            BigDecimal gainLoss
    ) {

        this.gainLoss = gainLoss;
    }

    public BigDecimal getGainLossPercent() {
        return gainLossPercent;
    }

    public void setGainLossPercent(
            BigDecimal gainLossPercent
    ) {

        this.gainLossPercent = gainLossPercent;
    }
}