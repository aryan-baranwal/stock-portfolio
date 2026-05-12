package com.portfolio.portfolio_service.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRICE-FETCHER-SERVICE")
public interface PriceFetcherClient {

    @GetMapping("/api/prices/{symbol}")
    String getStockPrice(
            @PathVariable String symbol
    );
}