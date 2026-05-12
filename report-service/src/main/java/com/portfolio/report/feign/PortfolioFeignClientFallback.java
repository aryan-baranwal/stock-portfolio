package com.portfolio.report.feign;

import com.portfolio.report.dto.ReportDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PortfolioFeignClientFallback implements PortfolioFeignClient {

    @Override
    public ReportDataDto getPortfolioSummary(Long id) {
        log.warn("Fallback triggered for portfolio-service getPortfolioSummary, portfolioId: {}", id);
        return new ReportDataDto(); // return empty DTO
    }
}