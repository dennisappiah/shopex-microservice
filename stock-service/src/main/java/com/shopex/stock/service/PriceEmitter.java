package com.shopex.stock.service;

import com.shopex.stock.dto.PriceUpdate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PriceEmitter {

    private static final Logger log = LoggerFactory.getLogger(PriceEmitter.class);
    private final ReactiveRedisTemplate<String, PriceUpdate> redisTemplate;

    @Value("${redis.channels.price-updates:price-updates}")
    private String priceUpdatesChannel;

    public Flux<PriceUpdate> getPriceStream() {
        return redisTemplate.listenToChannel(priceUpdatesChannel)
                .flatMap(message -> convertToPriceUpdate(message.getMessage()))
                .onErrorResume(e -> {
                    log.error("Error processing Redis message", e);
                    return Mono.empty();
                })
                .publish()
                .autoConnect();
    }

    private Mono<PriceUpdate> convertToPriceUpdate(Object message) {
        try {
            if (message instanceof PriceUpdate) {
                return Mono.just((PriceUpdate) message);
            }
            return Mono.error(new IllegalArgumentException(
                    "Unsupported message type: " + message.getClass().getName()));
        } catch (Exception e) {
            log.error("Failed to convert message to PriceUpdate", e);
            return Mono.error(e);
        }
    }

    public Mono<Long> publishPriceUpdate(PriceUpdate update) {
        if (update == null) {
            return Mono.error(new IllegalArgumentException("PriceUpdate cannot be null"));
        }
        return redisTemplate.convertAndSend(priceUpdatesChannel, update)
                .doOnError(e -> log.error("Failed to publish price update", e))
                .doOnSuccess(count ->
                        log.debug("Published price update to {} subscribers", count));
    }
}