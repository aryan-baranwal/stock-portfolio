package com.portfolio.report.feign;

import com.portfolio.report.dto.ReportDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "portfolio-service", fallback = PortfolioFeignClientFallback.class)
public interface PortfolioFeignClient {

    @GetMapping("/api/portfolios/{id}/summary")
    ReportDataDto getPortfolioSummary(@PathVariable Long id);
}