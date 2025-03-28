package com.shopex.stock.controller;

import com.shopex.common.domain.Ticker;
import com.shopex.stock.dto.PriceUpdate;
import com.shopex.stock.service.PriceEmitter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Slf4j
public class PriceController {

    private final PriceEmitter priceEmitter;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PriceUpdate> streamPrices() {
        return priceEmitter.getPriceStream()
                .doOnError(e -> log.error("Stream error", e))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .limitRate(10)
                .mergeWith(heartbeatFlux());
    }

    private Flux<PriceUpdate> heartbeatFlux() {
        return Flux.interval(Duration.ofSeconds(30))
                .map(tick -> new PriceUpdate(
                        Ticker.GOOGLE, // Create a Ticker object
                        0,                      // Integer price
                        LocalDateTime.now()     // Use LocalDateTime
                ));
    }
}