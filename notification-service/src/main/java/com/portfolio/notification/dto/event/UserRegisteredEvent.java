package com.portfolio.notification.dto.event;

import lombok.Data;

@Data
public class UserRegisteredEvent {
    private Long userId;
    private String email;
    private String fullName;
    private String registeredAt;
}