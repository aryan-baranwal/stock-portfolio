package com.portfolio.portfolio_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePortfolioRequestDto {

    @NotBlank(message = "Portfolio name is required")
    @Size(max = 100, message = "Portfolio name cannot exceed 100 characters")
    private String name;

    private String description;

    @NotBlank(message = "Currency is required")
    private String currency;

    public CreatePortfolioRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}