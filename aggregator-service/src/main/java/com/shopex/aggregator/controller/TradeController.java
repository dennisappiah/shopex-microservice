package com.shopex.aggregator.controller;


import com.shopex.aggregator.service.TradeOrchestrator;
import com.shopex.common.dto.StockTradeResponse;
import com.shopex.common.dto.TradeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {
    private final TradeOrchestrator orchestrator;

    @PostMapping
    public Mono<StockTradeResponse> executeTrade(@RequestBody TradeRequest request) {
        return orchestrator.executeTrade(request)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> Mono.just(null));
    }
}