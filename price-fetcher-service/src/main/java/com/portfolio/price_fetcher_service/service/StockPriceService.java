package com.portfolio.price_fetcher_service.service;

import com.portfolio.price_fetcher_service.client.StockApiClient;
import com.portfolio.price_fetcher_service.entity.PriceHistory;
import com.portfolio.price_fetcher_service.event.PriceUpdatedEvent;
import com.portfolio.price_fetcher_service.repository.PriceHistoryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.concurrent.TimeUnit;

@Service
public class StockPriceService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    StockPriceService.class
            );

    private final StockApiClient stockApiClient;

    private final RedisTemplate<String, Object> redisTemplate;

    private final PriceHistoryRepository priceHistoryRepository;

    private final RabbitMQPublisher rabbitMQPublisher;

    public StockPriceService(
            StockApiClient stockApiClient,
            RedisTemplate<String, Object> redisTemplate,
            PriceHistoryRepository priceHistoryRepository,
            RabbitMQPublisher rabbitMQPublisher
    ) {

        this.stockApiClient = stockApiClient;
        this.redisTemplate = redisTemplate;
        this.priceHistoryRepository = priceHistoryRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    @Cacheable(
            value = "stocks",
            key = "#symbol"
    )
    public String getStockPrice(String symbol) {

        String cacheKey = "stock::" + symbol;

        logger.debug(
                "Generated Redis cache key: {}",
                cacheKey
        );

        Object cachedData =
                redisTemplate.opsForValue().get(cacheKey);

        if (cachedData != null) {

            logger.info(
                    "Fetching stock price from Redis cache for symbol: {}",
                    symbol
            );

            return cachedData.toString();
        }

        logger.warn(
                "Cache miss for symbol: {}. Fetching from external API.",
                symbol
        );

        String freshPrice;

        try {

            freshPrice =
                    stockApiClient.fetchPrice(symbol);

        } catch (Exception ex) {

            logger.error(
                    "Failed to fetch stock price for symbol: {}",
                    symbol,
                    ex
            );

            throw ex;
        }

        logger.info(
                "Fetching stock price from external API for symbol: {}",
                symbol
        );

        redisTemplate.opsForValue().set(
                cacheKey,
                freshPrice,
                5,
                TimeUnit.MINUTES
        );

        PriceHistory history =
                new PriceHistory(
                        symbol,
                        freshPrice,
                        LocalDateTime.now()
                );

        priceHistoryRepository.save(history);

        logger.info(
                "Price history saved successfully for symbol: {}",
                symbol
        );

        PriceUpdatedEvent event =
                new PriceUpdatedEvent(
                        symbol,
                        freshPrice,
                        LocalDateTime.now()
                );

        rabbitMQPublisher.publishPriceUpdate(event);

        logger.info(
                "RabbitMQ event published for symbol: {}",
                symbol
        );

        return freshPrice;
    }

    @CacheEvict(
            value = "stocks",
            key = "#symbol"
    )
    public void refreshStockPrice(String symbol) {

        logger.info(
                "Refreshing stock cache manually for symbol: {}",
                symbol
        );

        String freshPrice;

        try {

            freshPrice =
                    stockApiClient.fetchPrice(symbol);

        } catch (Exception ex) {

            logger.error(
                    "Failed to refresh stock price for symbol: {}",
                    symbol,
                    ex
            );

            throw ex;
        }

        redisTemplate.opsForValue().set(
                "stock::" + symbol,
                freshPrice,
                5,
                TimeUnit.MINUTES
        );

        PriceHistory history =
                new PriceHistory(
                        symbol,
                        freshPrice,
                        LocalDateTime.now()
                );

        priceHistoryRepository.save(history);

        PriceUpdatedEvent event =
                new PriceUpdatedEvent(
                        symbol,
                        freshPrice,
                        LocalDateTime.now()
                );

        rabbitMQPublisher.publishPriceUpdate(event);

        logger.info(
                "Cache refreshed successfully for symbol: {}",
                symbol
        );
    }

    public Map<String, String> getBatchPrices(
            List<String> symbols
    ) {

        logger.info(
                "Fetching batch stock prices for symbols: {}",
                symbols
        );

        Map<String, String> prices =
                new HashMap<>();

        for (String symbol : symbols) {

            String price =
                    getStockPrice(symbol);

            prices.put(symbol, price);
        }

        return prices;
    }

    public List<PriceHistory> getPriceHistory(
            String symbol
    ) {

        logger.info(
                "Fetching price history for symbol: {}",
                symbol
        );

        return priceHistoryRepository
                .findBySymbol(symbol);
    }

    public Map<String, Object> getCacheStatus() {

        logger.info(
                "Fetching Redis cache statistics"
        );

        Map<String, Object> cacheStats =
                new HashMap<>();

        Set<String> keys =
                redisTemplate.keys("stock::*");

        cacheStats.put(
                "cacheServer",
                "Redis"
        );

        cacheStats.put(
                "cachedKeys",
                keys
        );

        cacheStats.put(
                "totalCachedStocks",
                keys != null ? keys.size() : 0
        );

        cacheStats.put(
                "status",
                "ACTIVE"
        );

        return cacheStats;
    }
}