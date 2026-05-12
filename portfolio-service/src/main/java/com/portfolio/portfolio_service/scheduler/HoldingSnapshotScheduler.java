package com.portfolio.portfolio_service.scheduler;

import com.portfolio.portfolio_service.service.HoldingSnapshotService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HoldingSnapshotScheduler {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    HoldingSnapshotScheduler.class
            );

    private final HoldingSnapshotService
            holdingSnapshotService;

    public HoldingSnapshotScheduler(
            HoldingSnapshotService holdingSnapshotService
    ) {

        this.holdingSnapshotService =
                holdingSnapshotService;
    }

    // every 30 seconds for testing
    @Scheduled(fixedRate = 30000)

    // production example:
    // @Scheduled(cron = "0 0 18 * * ?")

    public void generateHoldingSnapshots() {

        logger.info(
                "Starting holding snapshot generation..."
        );

        holdingSnapshotService.generateSnapshots();

        logger.info(
                "Holding snapshot generation completed."
        );
    }
}