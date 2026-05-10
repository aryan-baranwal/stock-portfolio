package com.portfolio.portfolio_service.service.impl;

import com.portfolio.portfolio_service.dto.CreatePortfolioRequestDto;
import com.portfolio.portfolio_service.dto.HoldingResponseDto;
import com.portfolio.portfolio_service.dto.PortfolioSummaryDto;
import com.portfolio.portfolio_service.entity.Portfolio;
import com.portfolio.portfolio_service.event.PortfolioSummaryReadyEvent;
import com.portfolio.portfolio_service.exception.ResourceNotFoundException;
import com.portfolio.portfolio_service.repository.PortfolioRepository;
import com.portfolio.portfolio_service.service.HoldingService;
import com.portfolio.portfolio_service.service.PortfolioEventPublisher;
import com.portfolio.portfolio_service.service.PortfolioService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PortfolioServiceImpl
        implements PortfolioService {

    private final PortfolioRepository portfolioRepository;

    private final HoldingService holdingService;

    private final PortfolioEventPublisher portfolioEventPublisher;

    public PortfolioServiceImpl(
            PortfolioRepository portfolioRepository,
            HoldingService holdingService,
            PortfolioEventPublisher portfolioEventPublisher
    ) {

        this.portfolioRepository = portfolioRepository;
        this.holdingService = holdingService;
        this.portfolioEventPublisher = portfolioEventPublisher;
    }

    @Override
    public Portfolio createPortfolio(
            CreatePortfolioRequestDto requestDto
    ) {

        Portfolio portfolio = new Portfolio();

        portfolio.setUserId(1L);

        portfolio.setName(
                requestDto.getName()
        );

        portfolio.setDescription(
                requestDto.getDescription()
        );

        portfolio.setCurrency(
                requestDto.getCurrency()
        );

        portfolio.setCreatedAt(
                LocalDateTime.now()
        );

        portfolio.setIsActive(true);

        return portfolioRepository.save(portfolio);
    }

    @Override
    public List<Portfolio> getAllPortfolios(
            Long userId
    ) {

        return portfolioRepository
                .findByUserIdAndIsActiveTrue(userId);
    }

    @Override
    public Portfolio getPortfolioById(
            Long portfolioId
    ) {

        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Portfolio not found"
                        )
                );
    }

    @Override
    public Portfolio updatePortfolio(
            Long portfolioId,
            CreatePortfolioRequestDto requestDto
    ) {

        Portfolio portfolio =
                getPortfolioById(portfolioId);

        portfolio.setName(
                requestDto.getName()
        );

        portfolio.setDescription(
                requestDto.getDescription()
        );

        portfolio.setCurrency(
                requestDto.getCurrency()
        );

        return portfolioRepository.save(portfolio);
    }

    @Override
    public void deletePortfolio(
            Long portfolioId
    ) {

        Portfolio portfolio =
                getPortfolioById(portfolioId);

        portfolio.setIsActive(false);

        portfolioRepository.save(portfolio);
    }

    @Override

    @Cacheable(
            value = "portfolio-summary",
            key = "#portfolioId"
    )

    public PortfolioSummaryDto getPortfolioSummary(
            Long portfolioId
    ) {

        Portfolio portfolio =
                getPortfolioById(portfolioId);

        List<HoldingResponseDto> holdings =
                holdingService.getHoldingsWithGainLoss(
                        portfolioId
                );

        BigDecimal totalInvestedValue =
                BigDecimal.ZERO;

        BigDecimal totalCurrentValue =
                BigDecimal.ZERO;

        for (HoldingResponseDto holding : holdings) {

            BigDecimal investedValue =
                    holding.getBuyPrice()
                            .multiply(
                                    holding.getQuantity()
                            );

            totalInvestedValue =
                    totalInvestedValue.add(
                            investedValue
                    );

            totalCurrentValue =
                    totalCurrentValue.add(
                            holding.getCurrentValue()
                    );
        }

        BigDecimal totalGainLoss =
                totalCurrentValue.subtract(
                        totalInvestedValue
                );

        BigDecimal totalGainLossPercent =
                BigDecimal.ZERO;

        if (totalInvestedValue.compareTo(BigDecimal.ZERO) > 0) {

            totalGainLossPercent =
                    totalGainLoss.divide(
                                    totalInvestedValue,
                                    4,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(
                                    BigDecimal.valueOf(100)
                            );
        }

        PortfolioSummaryDto dto =
                new PortfolioSummaryDto();

        dto.setPortfolioId(
                portfolio.getId()
        );

        dto.setName(
                portfolio.getName()
        );

        dto.setCurrency(
                portfolio.getCurrency()
        );

        dto.setHoldingCount(
                holdings.size()
        );

        dto.setTotalInvestedValue(
                totalInvestedValue
        );

        dto.setTotalCurrentValue(
                totalCurrentValue
        );

        dto.setTotalGainLoss(
                totalGainLoss
        );

        dto.setTotalGainLossPercent(
                totalGainLossPercent
        );

        dto.setAsOfTime(
                LocalDateTime.now()
        );

        PortfolioSummaryReadyEvent event =
                new PortfolioSummaryReadyEvent(
                        dto.getPortfolioId(),
                        dto.getName(),
                        dto.getTotalCurrentValue(),
                        dto.getTotalGainLoss(),
                        LocalDateTime.now()
                );

        portfolioEventPublisher
                .publishPortfolioSummary(event);

        return dto;
    }
}