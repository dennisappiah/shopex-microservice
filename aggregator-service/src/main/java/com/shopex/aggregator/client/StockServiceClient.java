package com.shopex.aggregator.client;

import com.shopex.common.domain.Ticker;
import com.shopex.common.dto.StockPriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockServiceClient {

    private final WebClient stockServiceWebClient;
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    public Mono<Integer> getLatestPriceAsInt(Ticker ticker) {
        return this.stockServiceWebClient.get()
                .uri("/stock/{ticker}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class)
                .timeout(TIMEOUT)
                .map(response -> response.price().intValue())
                .doOnError(e -> log.error("Failed to fetch stock price", e));
    }
}