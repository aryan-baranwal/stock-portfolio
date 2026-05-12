package com.portfolio.price_fetcher_service.controller;

import com.portfolio.price_fetcher_service.entity.PriceHistory;
import com.portfolio.price_fetcher_service.service.StockPriceService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    public StockPriceController(
            StockPriceService stockPriceService
    ) {

        this.stockPriceService = stockPriceService;
    }

    @Operation(summary = "Get stock price by symbol")
    @GetMapping("/{symbol}")
    public String getPrice(
            @PathVariable String symbol
    ) {

        return stockPriceService.getStockPrice(symbol);
    }

    @Operation(summary = "Get multiple stock prices")
    @GetMapping("/batch")
    public Map<String, String> getBatchPrices(
            @RequestParam List<String> symbols
    ) {

        return stockPriceService.getBatchPrices(symbols);
    }

    @Operation(summary = "Refresh stock cache manually")
    @PostMapping("/refresh/{symbol}")
    public String refreshPrice(
            @PathVariable String symbol
    ) {

        stockPriceService.refreshStockPrice(symbol);

        return "Price refreshed successfully for " + symbol;
    }

    @Operation(summary = "Get stock price history")
    @GetMapping("/history/{symbol}")
    public List<PriceHistory> getHistory(
            @PathVariable String symbol
    ) {

        return stockPriceService
                .getPriceHistory(symbol);
    }

    @Operation(summary = "Get Redis cache statistics")
    @GetMapping("/cache/status")
    public Map<String, Object> getCacheStatus() {

        return stockPriceService
                .getCacheStatus();
    }
}