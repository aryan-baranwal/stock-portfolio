package com.portfolio.auth.messaging;

import com.portfolio.auth.entity.User;
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
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key.user-registered}")
    private String userRegisteredRoutingKey;

    public void publishUserRegistered(User user) {
        Map<String, Object> event = new HashMap<>();
        event.put("userId", user.getId());
        event.put("email", user.getEmail());
        event.put("fullName", user.getFullName());
        event.put("registeredAt", LocalDateTime.now().toString());

        rabbitTemplate.convertAndSend(exchange, userRegisteredRoutingKey, event);
        log.info("Published UserRegistered event for userId: {}", user.getId());
    }
}