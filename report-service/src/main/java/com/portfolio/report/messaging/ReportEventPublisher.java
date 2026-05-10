package com.portfolio.report.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key.daily-summary}")
    private String dailySummaryRoutingKey;

    public void publishDailySummaryReady(Long userId, Long portfolioId) {
        Map<String, Object> event = new HashMap<>();
        event.put("userId", userId);
        event.put("portfolioId", portfolioId);
        event.put("reportId", "RPT-" + userId + "-" + System.currentTimeMillis());
        event.put("generatedAt", LocalDateTime.now().toString());

        rabbitTemplate.convertAndSend(exchange, dailySummaryRoutingKey, event);
        log.info("Published DailySummaryReady event for userId: {}", userId);
    }
}