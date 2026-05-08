package com.portfolio.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Main Exchange
    @Bean
    public TopicExchange portfolioExchange() {
        return new TopicExchange("portfolio.exchange");
    }

    // Dead Letter Exchange
    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange("portfolio.dlx");
    }

    // Notification queue (for UserRegistered)
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable("notification.queue")
                .withArgument("x-dead-letter-exchange", "portfolio.dlx")
                .build();
    }

    // Alert notification queue
    @Bean
    public Queue alertNotificationQueue() {
        return QueueBuilder.durable("alert.notification.queue")
                .withArgument("x-dead-letter-exchange", "portfolio.dlx")
                .build();
    }

    // Report notification queue
    @Bean
    public Queue reportNotificationQueue() {
        return QueueBuilder.durable("report.notification.queue")
                .withArgument("x-dead-letter-exchange", "portfolio.dlx")
                .build();
    }

    // DLQ
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("notification.dlq").build();
    }

    // Bindings
    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange portfolioExchange) {
        return BindingBuilder.bind(notificationQueue).to(portfolioExchange).with("user.registered");
    }

    @Bean
    public Binding alertNotificationBinding(Queue alertNotificationQueue, TopicExchange portfolioExchange) {
        return BindingBuilder.bind(alertNotificationQueue).to(portfolioExchange).with("alert.triggered");
    }

    @Bean
    public Binding reportNotificationBinding(Queue reportNotificationQueue, TopicExchange portfolioExchange) {
        return BindingBuilder.bind(reportNotificationQueue).to(portfolioExchange).with("report.daily_summary");
    }

    @Bean
    public Binding dlqBinding(Queue deadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with("#");
    }

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