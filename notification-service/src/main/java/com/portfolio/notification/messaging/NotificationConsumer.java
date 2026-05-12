package com.portfolio.notification.messaging;

import com.portfolio.notification.dto.event.AlertTriggeredEvent;
import com.portfolio.notification.dto.event.DailySummaryEvent;
import com.portfolio.notification.dto.event.UserRegisteredEvent;
import com.portfolio.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Received UserRegistered event for userId: {}", event.getUserId());
        notificationService.handleUserRegistered(event);
    }

    @RabbitListener(queues = "alert.notification.queue")
    public void handleAlertTriggered(AlertTriggeredEvent event) {
        log.info("Received AlertTriggered event for userId: {}", event.getUserId());
        notificationService.handleAlertTriggered(event);
    }

    @RabbitListener(queues = "report.notification.queue")
    public void handleDailySummary(DailySummaryEvent event) {
        log.info("Received DailySummary event for userId: {}", event.getUserId());
        notificationService.handleDailySummary(event);
    }
}