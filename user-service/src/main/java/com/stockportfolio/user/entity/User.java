package com.stockportfolio.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phone;

    private String timezone;

    private String currency;

    private Boolean deleted = false;

    @Column(name = "email_notifications")
    private boolean emailNotifications;

    @Column(name = "price_alert_email")
    private boolean priceAlertEmail;

    @Column(name = "daily_summary_email")
    private boolean dailySummaryEmail;

    @Column(name = "preferred_currency")
    private String preferredCurrency;
}