package com.portfolio.alert.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "portfolio-service")
public interface PortfolioFeignClient {

    // Called during PORTFOLIO_LOSS_PERCENT alert evaluation
    // Fetches the latest portfolio summary (gain/loss data)
    @GetMapping("/api/portfolios/{id}/summary")
    PortfolioSummaryDto getPortfolioSummary(@PathVariable Long id);
}