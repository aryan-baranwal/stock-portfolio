package com.portfolio.alert.entity;

import com.portfolio.alert.enums.AlertCondition;
import com.portfolio.alert.enums.AlertStatus;
import com.portfolio.alert.enums.AlertType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;

    private String stockSymbol;

    private BigDecimal targetPrice;

    private Long portfolioId;

    private BigDecimal lossThresholdPercent;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_condition")
    private AlertCondition condition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AlertStatus status = AlertStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastTriggeredAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}