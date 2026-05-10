package com.portfolio.alert.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "alert_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long alertId;

    @Column(nullable = false)
    private Long userId;

    private String stockSymbol;

    private BigDecimal priceAtTrigger;

    private BigDecimal targetPrice;

    private String alertTypeSnapshot;

    @Column(nullable = false)
    private LocalDateTime triggeredAt;

    @PrePersist
    protected void onCreate() {
        this.triggeredAt = LocalDateTime.now();
    }
}