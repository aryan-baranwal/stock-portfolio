package com.portfolio.alert.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange name — shared across all services
    public static final String EXCHANGE = "portfolio.exchange";
    public static final String DLX = "portfolio.dlx";

    // Queue names
    public static final String ALERT_QUEUE = "alert.queue";
    public static final String ALERT_DLQ = "alert.dlq";

    // Routing keys
    public static final String PRICE_UPDATED_KEY = "price.updated";

    // ── Exchange ──────────────────────────────────────────
    @Bean
    public TopicExchange portfolioExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX);
    }

    // ── Queues ────────────────────────────────────────────
    @Bean
    public Queue alertQueue() {
        // If a message fails 3 times, it goes to the DLQ
        return QueueBuilder.durable(ALERT_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .build();
    }

    @Bean
    public Queue alertDeadLetterQueue() {
        return QueueBuilder.durable(ALERT_DLQ).build();
    }

    // ── Bindings ──────────────────────────────────────────
    @Bean
    public Binding alertBinding(Queue alertQueue,
                                TopicExchange portfolioExchange) {
        // This queue listens for "price.updated" messages
        return BindingBuilder
                .bind(alertQueue)
                .to(portfolioExchange)
                .with(PRICE_UPDATED_KEY);
    }

    // ── JSON Message Converter ────────────────────────────
    // Tells RabbitMQ to send/receive messages as JSON
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}