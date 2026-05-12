package com.portfolio.portfolio_service.service.impl;

import com.portfolio.portfolio_service.dto.HoldingResponseDto;
import com.portfolio.portfolio_service.entity.Holding;
import com.portfolio.portfolio_service.entity.HoldingSnapshot;

import com.portfolio.portfolio_service.repository.HoldingRepository;
import com.portfolio.portfolio_service.repository.HoldingSnapshotRepository;

import com.portfolio.portfolio_service.service.HoldingService;
import com.portfolio.portfolio_service.service.HoldingSnapshotService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HoldingSnapshotServiceImpl
        implements HoldingSnapshotService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    HoldingSnapshotServiceImpl.class
            );

    private final HoldingRepository holdingRepository;

    private final HoldingSnapshotRepository
            snapshotRepository;

    private final HoldingService holdingService;

    public HoldingSnapshotServiceImpl(
            HoldingRepository holdingRepository,
            HoldingSnapshotRepository snapshotRepository,
            HoldingService holdingService
    ) {

        this.holdingRepository = holdingRepository;
        this.snapshotRepository = snapshotRepository;
        this.holdingService = holdingService;
    }

    @Override
    public void generateSnapshots() {

        List<Holding> holdings =
                holdingRepository.findAll();

        for (Holding holding : holdings) {

            if (Boolean.TRUE.equals(
                    holding.getIsDeleted()
            )) {

                continue;
            }

            HoldingResponseDto response =
                    holdingService.getHoldingWithGainLoss(
                            holding.getId()
                    );

            HoldingSnapshot snapshot =
                    new HoldingSnapshot();

            snapshot.setHolding(holding);

            snapshot.setSnapshotDate(
                    LocalDateTime.now()
            );

            snapshot.setCurrentPrice(
                    response.getCurrentPrice()
            );

            snapshot.setGainLoss(
                    response.getGainLoss()
            );

            snapshot.setGainLossPercent(
                    response.getGainLossPercent()
            );

            snapshotRepository.save(snapshot);

            logger.info(
                    "Snapshot generated for holding: {}",
                    holding.getId()
            );
        }
    }
}