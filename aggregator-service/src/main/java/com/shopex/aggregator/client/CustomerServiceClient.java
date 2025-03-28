package com.shopex.aggregator.client;

import com.shopex.common.dto.StockTradeResponse;
import com.shopex.common.dto.TradeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceClient {

    private final WebClient customerServiceWebClient;
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    public Mono<StockTradeResponse> executeTrade(TradeRequest request) {
        return customerServiceWebClient.post()
                .uri("/api/trades")  // relative to base URL
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StockTradeResponse.class)
                .timeout(TIMEOUT)
                .doOnError(e -> log.error("Failed to execute trade", e));
}

}