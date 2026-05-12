package com.portfolio.price_fetcher_service.service;

import com.portfolio.price_fetcher_service.event.PriceUpdatedEvent;
import com.portfolio.price_fetcher_service.rabbitmq.RabbitMQConfig;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPublisher(
            RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPriceUpdate(
            PriceUpdatedEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        System.out.println(
                "Published Event for: "
                        + event.getSymbol()
        );
    }
}