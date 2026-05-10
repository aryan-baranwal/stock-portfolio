package com.portfolio.alert.messaging;

import com.portfolio.alert.entity.Alert;
import com.portfolio.alert.entity.AlertHistory;
import com.portfolio.alert.enums.AlertStatus;
import com.portfolio.alert.enums.AlertType;
import com.portfolio.alert.feign.PortfolioFeignClient;
import com.portfolio.alert.feign.PortfolioSummaryDto;
import com.portfolio.alert.repository.AlertHistoryRepository;
import com.portfolio.alert.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceUpdateConsumer {

    private final AlertRepository alertRepository;
    private final AlertHistoryRepository alertHistoryRepository;
    private final AlertEventPublisher alertEventPublisher;
    private final PortfolioFeignClient portfolioFeignClient;

    @RabbitListener(queues = "alert.queue")
    public void consumePriceUpdate(PriceUpdatedEvent event) {
        log.info("Received PriceUpdated event for symbol: {}, price: {}",
                event.getSymbol(), event.getCurrentPrice());

        // Step 1 — Find all ACTIVE alerts for this stock symbol
        List<Alert> activeAlerts = alertRepository
                .findByStockSymbolAndStatusAndIsDeletedFalse(
                        event.getSymbol(), AlertStatus.ACTIVE);

        // Step 2 — Evaluate each alert
        for (Alert alert : activeAlerts) {
            evaluateAlert(alert, event);
        }
    }

    private void evaluateAlert(Alert alert, PriceUpdatedEvent event) {
        boolean shouldFire = false;

        if (alert.getAlertType() == AlertType.PRICE_THRESHOLD) {
            // Fire if price crosses the target in the configured direction
            shouldFire = evaluatePriceThreshold(alert, event);

        } else if (alert.getAlertType() == AlertType.PORTFOLIO_LOSS_PERCENT) {
            // Fire if portfolio loss exceeds the threshold
            shouldFire = evaluatePortfolioLoss(alert);
        }

        if (shouldFire) {
            fireAlert(alert, event);
        }
    }

    private boolean evaluatePriceThreshold(Alert alert, PriceUpdatedEvent event) {
        if (alert.getCondition() == null || alert.getTargetPrice() == null) {
            return false;
        }

        switch (alert.getCondition()) {
            case ABOVE:
                return event.getCurrentPrice()
                        .compareTo(alert.getTargetPrice()) >= 0;
            case BELOW:
                return event.getCurrentPrice()
                        .compareTo(alert.getTargetPrice()) <= 0;
            default:
                return false;
        }
    }

    private boolean evaluatePortfolioLoss(Alert alert) {
        if (alert.getPortfolioId() == null
                || alert.getLossThresholdPercent() == null) {
            return false;
        }

        try {
            // Feign call to portfolio-service to get latest gain/loss
            PortfolioSummaryDto summary = portfolioFeignClient
                    .getPortfolioSummary(alert.getPortfolioId());

            // Fire if loss % exceeds the threshold (e.g. loss > 10%)
            return summary.getTotalGainLossPercent()
                    .compareTo(alert.getLossThresholdPercent().negate()) <= 0;

        } catch (Exception e) {
            log.error("Failed to fetch portfolio summary for portfolioId: {}",
                    alert.getPortfolioId(), e);
            return false;
        }
    }

    private void fireAlert(Alert alert, PriceUpdatedEvent event) {
        log.info("Firing alert id: {} for user: {}", alert.getId(), alert.getUserId());

        // Step 1 — Update alert status to TRIGGERED
        alert.setStatus(AlertStatus.TRIGGERED);
        alert.setLastTriggeredAt(LocalDateTime.now());
        alertRepository.save(alert);

        // Step 2 — Save a record in alert_history table
        AlertHistory history = AlertHistory.builder()
                .alertId(alert.getId())
                .userId(alert.getUserId())
                .stockSymbol(alert.getStockSymbol())
                .priceAtTrigger(event.getCurrentPrice())
                .targetPrice(alert.getTargetPrice())
                .alertTypeSnapshot(alert.getAlertType().name())
                .build();
        alertHistoryRepository.save(history);

        // Step 3 — Publish event to RabbitMQ → notification-service will send email
        AlertTriggeredEvent triggeredEvent = AlertTriggeredEvent.builder()
                .alertId(alert.getId())
                .userId(alert.getUserId())
                .alertType(alert.getAlertType().name())
                .stockSymbol(alert.getStockSymbol())
                .targetPrice(alert.getTargetPrice())
                .currentPrice(event.getCurrentPrice())
                .triggeredAt(LocalDateTime.now())
                .build();

        alertEventPublisher.publishAlertTriggered(triggeredEvent);
    }
}