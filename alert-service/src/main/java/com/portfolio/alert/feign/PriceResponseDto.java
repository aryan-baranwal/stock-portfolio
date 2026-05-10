package com.portfolio.alert.feign;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceResponseDto {

    private String symbol;
    private BigDecimal currentPrice;
    private BigDecimal previousPrice;
    private BigDecimal changePercent;
    private LocalDateTime updatedAt;
}