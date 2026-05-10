package com.portfolio.portfolio_service.service;

import com.portfolio.portfolio_service.dto.HoldingRequestDto;
import com.portfolio.portfolio_service.dto.HoldingResponseDto;
import com.portfolio.portfolio_service.entity.Holding;

import java.util.List;

public interface HoldingService {

    Holding addHolding(
            HoldingRequestDto requestDto
    );

    List<Holding> getHoldingsByPortfolio(
            Long portfolioId
    );

    Holding getHoldingById(
            Long holdingId
    );

    Holding updateHolding(
            Long holdingId,
            HoldingRequestDto requestDto
    );

    void deleteHolding(
            Long holdingId
    );

    HoldingResponseDto getHoldingWithGainLoss(
            Long holdingId
    );

    List<HoldingResponseDto> getHoldingsWithGainLoss(
            Long portfolioId
    );
}