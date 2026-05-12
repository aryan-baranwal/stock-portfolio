package com.portfolio.price_fetcher_service.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PriceUpdatedEvent implements Serializable {

    private String symbol;
    private String price;
    private LocalDateTime timestamp;

    public PriceUpdatedEvent() {
    }

    public PriceUpdatedEvent(
            String symbol,
            String price,
            LocalDateTime timestamp
    ) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}