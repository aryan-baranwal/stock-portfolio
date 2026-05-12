package com.portfolio.price_fetcher_service.scheduler;

import com.portfolio.price_fetcher_service.service.StockPriceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockPriceScheduler {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    StockPriceScheduler.class
            );

    private final StockPriceService stockPriceService;

    public StockPriceScheduler(
            StockPriceService stockPriceService
    ) {

        this.stockPriceService = stockPriceService;
    }

    /*
        Runs every 5 minutes

        Cron Format:
        second minute hour day month weekday
    */

    @Scheduled(cron = "0 */5 * * * *")
    public void refreshStockPrices() {

        logger.info(
                "Starting scheduled stock price refresh..."
        );

        try {

            stockPriceService.refreshStockPrice("TSLA");

            stockPriceService.refreshStockPrice("AAPL");

            stockPriceService.refreshStockPrice("MSFT");

            logger.info(
                    "Scheduled stock refresh completed successfully"
            );

        } catch (Exception ex) {

            logger.error(
                    "Error occurred during scheduled stock refresh",
                    ex
            );
        }
    }
}