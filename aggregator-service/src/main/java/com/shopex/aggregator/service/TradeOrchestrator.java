package com.shopex.aggregator.service;

import com.shopex.aggregator.client.CustomerServiceClient;
import com.shopex.aggregator.client.StockServiceClient;
import com.shopex.common.dto.StockTradeResponse;
import com.shopex.common.dto.TradeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeOrchestrator {
    private final StockServiceClient stockClient;
    private final CustomerServiceClient customerClient;

    public Mono<StockTradeResponse> executeTrade(TradeRequest request) {
        log.info("Executing trade for ticker: {}", request.ticker());

        return stockClient.getLatestPriceAsInt(request.ticker())
                .map(Integer::doubleValue)
                .flatMap(price -> validatePrice(price, request))
                .flatMap(validated -> customerClient.executeTrade(request))
                .doOnError(e -> log.error("Trade failed for {}: {}",
                        request.ticker(), e.getMessage()))
                .onErrorResume(e -> Mono.empty()); // Gracefully handle errors
    }

    private Mono<Boolean> validatePrice(Double price, TradeRequest request) {
        if (price <= 0) {
            log.warn("Invalid price {} for {}", price, request.ticker());
            return Mono.error(new IllegalArgumentException("Price must be positive"));
        }
        return Mono.just(true);
    }
}