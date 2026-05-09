package com.portfolio.price_fetcher_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockPriceResponse {

    private String symbol;
    private Double price;
}