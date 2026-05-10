package com.portfolio.alert.messaging;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceUpdatedEvent {

    private String symbol;
    private BigDecimal currentPrice;
    private BigDecimal previousPrice;
    private BigDecimal changePercent;
    private LocalDateTime updatedAt;
}