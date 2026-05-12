package com.portfolio.portfolio_service.service;

import com.portfolio.portfolio_service.dto.CreatePortfolioRequestDto;
import com.portfolio.portfolio_service.dto.PortfolioSummaryDto;
import com.portfolio.portfolio_service.entity.Portfolio;

import java.util.List;

public interface PortfolioService {

    Portfolio createPortfolio(
            CreatePortfolioRequestDto requestDto
    );

    List<Portfolio> getAllPortfolios(
            Long userId
    );

    Portfolio getPortfolioById(
            Long portfolioId
    );

    Portfolio updatePortfolio(
            Long portfolioId,
            CreatePortfolioRequestDto requestDto
    );

    void deletePortfolio(
            Long portfolioId
    );

    PortfolioSummaryDto getPortfolioSummary(
            Long portfolioId
    );
}