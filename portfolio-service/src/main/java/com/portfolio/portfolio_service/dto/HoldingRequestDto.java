package com.portfolio.portfolio_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HoldingRequestDto {

    @NotNull(message = "Portfolio ID is required")
    private Long portfolioId;

    @NotBlank(message = "Stock symbol is required")
    @Size(max = 10, message = "Stock symbol cannot exceed 10 characters")
    private String stockSymbol;

    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @Positive(message = "Buy price must be positive")
    private BigDecimal buyPrice;

    @PastOrPresent(message = "Buy date cannot be in the future")
    private LocalDate buyDate;

    public HoldingRequestDto() {
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public LocalDate getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(LocalDate buyDate) {
        this.buyDate = buyDate;
    }
}