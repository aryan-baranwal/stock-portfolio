package com.portfolio.alert.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "price-fetcher-service")
public interface PriceFeignClient {

    // Called for on-demand price check during alert evaluation
    @GetMapping("/api/prices/{symbol}")
    PriceResponseDto getCurrentPrice(@PathVariable String symbol);
}