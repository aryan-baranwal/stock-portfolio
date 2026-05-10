package com.portfolio.alert.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public static final String EXCHANGE = "portfolio.exchange";
    public static final String ALERT_TRIGGERED_ROUTING_KEY = "alert.triggered";

    public void publishAlertTriggered(AlertTriggeredEvent event) {
        log.info("Publishing AlertTriggered event for alertId: {}, symbol: {}",
                event.getAlertId(), event.getStockSymbol());

        rabbitTemplate.convertAndSend(EXCHANGE, ALERT_TRIGGERED_ROUTING_KEY, event);

        log.info("AlertTriggered event published successfully");
    }
}