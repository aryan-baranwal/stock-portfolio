package com.portfolio.notification.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadLetterProcessor {

    @RabbitListener(queues = "${rabbitmq.queues.dlq}")
    public void processDlq(Object message) {
        log.error("Message moved to DLQ (all retries exhausted): {}", message);
        // Could persist to DB for admin requeue
    }
}