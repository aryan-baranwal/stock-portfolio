package com.portfolio.portfolio_service.service.impl;

import com.portfolio.portfolio_service.client.PriceFetcherClient;
import com.portfolio.portfolio_service.dto.HoldingRequestDto;
import com.portfolio.portfolio_service.dto.HoldingResponseDto;
import com.portfolio.portfolio_service.entity.Holding;
import com.portfolio.portfolio_service.entity.Portfolio;
import com.portfolio.portfolio_service.exception.ResourceNotFoundException;
import com.portfolio.portfolio_service.repository.HoldingRepository;
import com.portfolio.portfolio_service.repository.PortfolioRepository;
import com.portfolio.portfolio_service.service.HoldingService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HoldingServiceImpl
        implements HoldingService {

    private final HoldingRepository holdingRepository;

    private final PortfolioRepository portfolioRepository;

    private final PriceFetcherClient priceFetcherClient;

    public HoldingServiceImpl(
            HoldingRepository holdingRepository,
            PortfolioRepository portfolioRepository,
            PriceFetcherClient priceFetcherClient
    ) {

        this.holdingRepository = holdingRepository;
        this.portfolioRepository = portfolioRepository;
        this.priceFetcherClient = priceFetcherClient;
    }

    @Override

    @CacheEvict(
            value = "portfolio-summary",
            key = "#requestDto.portfolioId"
    )

    public Holding addHolding(
            HoldingRequestDto requestDto
    ) {

        Portfolio portfolio =
                portfolioRepository.findById(
                        requestDto.getPortfolioId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Portfolio not found"
                        )
                );

        Holding holding = new Holding();

        holding.setPortfolio(portfolio);

        holding.setStockSymbol(
                requestDto.getStockSymbol()
        );

        holding.setQuantity(
                requestDto.getQuantity()
        );

        holding.setBuyPrice(
                requestDto.getBuyPrice()
        );

        holding.setBuyDate(
                requestDto.getBuyDate()
        );

        holding.setCreatedAt(
                LocalDateTime.now()
        );

        holding.setIsDeleted(false);

        return holdingRepository.save(holding);
    }

    @Override
    public List<Holding> getHoldingsByPortfolio(
            Long portfolioId
    ) {

        return holdingRepository
                .findActiveHoldingsByPortfolio(
                        portfolioId
                );
    }

    @Override
    public Holding getHoldingById(
            Long holdingId
    ) {

        return holdingRepository.findById(holdingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Holding not found"
                        )
                );
    }

    @Override

    @CacheEvict(
            value = "portfolio-summary",
            allEntries = true
    )

    public Holding updateHolding(
            Long holdingId,
            HoldingRequestDto requestDto
    ) {

        Holding holding =
                getHoldingById(holdingId);

        holding.setQuantity(
                requestDto.getQuantity()
        );

        holding.setBuyPrice(
                requestDto.getBuyPrice()
        );

        holding.setBuyDate(
                requestDto.getBuyDate()
        );

        return holdingRepository.save(holding);
    }

    @Override

    @CacheEvict(
            value = "portfolio-summary",
            allEntries = true
    )

    public void deleteHolding(
            Long holdingId
    ) {

        Holding holding =
                getHoldingById(holdingId);

        holding.setIsDeleted(true);

        holdingRepository.save(holding);
    }

    @Override
    public HoldingResponseDto getHoldingWithGainLoss(
            Long holdingId
    ) {

        Holding holding =
                getHoldingById(holdingId);

        String currentPriceResponse =
                priceFetcherClient.getStockPrice(
                        holding.getStockSymbol()
                );

        BigDecimal currentPrice =
                new BigDecimal(currentPriceResponse);

        BigDecimal quantity =
                holding.getQuantity();

        BigDecimal investedValue =
                holding.getBuyPrice()
                        .multiply(quantity);

        BigDecimal currentValue =
                currentPrice.multiply(quantity);

        BigDecimal gainLoss =
                currentValue.subtract(investedValue);

        BigDecimal gainLossPercent =
                gainLoss.divide(
                                investedValue,
                                4,
                                RoundingMode.HALF_UP
                        )
                        .multiply(
                                BigDecimal.valueOf(100)
                        );

        HoldingResponseDto dto =
                new HoldingResponseDto();

        dto.setId(
                holding.getId()
        );

        dto.setStockSymbol(
                holding.getStockSymbol()
        );

        dto.setQuantity(
                quantity
        );

        dto.setBuyPrice(
                holding.getBuyPrice()
        );

        dto.setBuyDate(
                holding.getBuyDate()
        );

        dto.setCurrentPrice(
                currentPrice
        );

        dto.setCurrentValue(
                currentValue
        );

        dto.setGainLoss(
                gainLoss
        );

        dto.setGainLossPercent(
                gainLossPercent
        );

        return dto;
    }

    @Override
    public List<HoldingResponseDto> getHoldingsWithGainLoss(
            Long portfolioId
    ) {

        List<Holding> holdings =
                getHoldingsByPortfolio(
                        portfolioId
                );

        List<HoldingResponseDto> responseList =
                new ArrayList<>();

        for (Holding holding : holdings) {

            HoldingResponseDto dto =
                    getHoldingWithGainLoss(
                            holding.getId()
                    );

            responseList.add(dto);
        }

        return responseList;
    }
}