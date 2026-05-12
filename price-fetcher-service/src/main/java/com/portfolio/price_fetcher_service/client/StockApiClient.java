package com.portfolio.price_fetcher_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class StockApiClient {

    @Value("${stock.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate =
            new RestTemplate();

    public String fetchPrice(String symbol) {

        try {

            String url =
                    "https://finnhub.io/api/v1/quote?symbol="
                            + symbol
                            + "&token="
                            + apiKey;

            Map<String, Object> response =
                    restTemplate.getForObject(
                            url,
                            Map.class
                    );

            if (response == null ||
                    response.get("c") == null) {

                throw new RuntimeException(
                        "Invalid API response"
                );
            }

            return response
                    .get("c")
                    .toString();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to fetch stock price for: "
                            + symbol
            );
        }
    }
}