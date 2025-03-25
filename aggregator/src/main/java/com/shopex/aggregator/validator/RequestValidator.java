package com.shopex.aggregator.validator;

import com.shopex.aggregator.dto.TradeRequest;
import com.shopex.aggregator.exceptions.AggregatorApplicationException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {
    public static UnaryOperator<Mono<TradeRequest>> validate() {
        return mono -> mono.filter(hasTicker())
                .switchIfEmpty(AggregatorApplicationException.missingTicker())
                .filter(hasTradeAction())
                .switchIfEmpty(AggregatorApplicationException.missingTradeAction())
                .filter(isValidQuantity())
                .switchIfEmpty(AggregatorApplicationException.invalidQuantity());
    }

    private static Predicate<TradeRequest> hasTicker() {
        return dto -> Objects.nonNull(dto.ticker());
    }

    private static Predicate<TradeRequest> hasTradeAction() {
        return dto -> Objects.nonNull(dto.tradeAction());
    }

    private static Predicate<TradeRequest> isValidQuantity() {
        return dto -> Objects.nonNull(dto.quantity()) && dto.quantity() > 0;
    }
}
