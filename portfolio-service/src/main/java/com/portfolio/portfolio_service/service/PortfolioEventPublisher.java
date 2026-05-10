package com.portfolio.portfolio_service.service;

import com.portfolio.portfolio_service.event.PortfolioSummaryReadyEvent;
import com.portfolio.portfolio_service.rabbitmq.RabbitMQConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PortfolioEventPublisher {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    PortfolioEventPublisher.class
            );

    private final RabbitTemplate rabbitTemplate;

    public PortfolioEventPublisher(
            RabbitTemplate rabbitTemplate
    ) {

        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPortfolioSummary(
            PortfolioSummaryReadyEvent event
    ) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        logger.info(
                "Published portfolio summary event for portfolio: {}",
                event.getPortfolioId()
        );
    }
}