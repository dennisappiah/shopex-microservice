package com.shopex.aggregator.client;

import com.shopex.aggregator.exceptions.AggregatorApplicationException;
import com.shopex.common.dto.CustomerInformation;
import com.shopex.common.dto.StockTradeRequest;
import com.shopex.common.dto.StockTradeResponse;
import com.shopex.common.exceptions.GlobalExceptions;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public class CustomerServiceClient {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceClient.class);

    private final WebClient client;

    public Mono<CustomerInformation> getCustomerInformation(UUID customerId) {
        return this.client.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(CustomerInformation.class)
                .onErrorResume(NotFound.class, ex -> GlobalExceptions.customerNotFound(customerId));
    }

    public Mono<StockTradeResponse> trade(UUID customerId, StockTradeRequest request) {
        return this.client.post()
                .uri("/customers/{customerId}/trade", customerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StockTradeResponse.class)
                .onErrorResume(NotFound.class, ex -> GlobalExceptions.customerNotFound(customerId))
                .onErrorResume(BadRequest.class, this::handleException);
    }

    private <T> Mono<T> handleException(BadRequest exception){
        var pd = exception.getResponseBodyAs(ProblemDetail.class);
        var message = Objects.nonNull(pd) ? pd.getDetail() : exception.getMessage();
        log.error("customer service problem detail: {}", pd);
        return AggregatorApplicationException.invalidTradeRequest(message);
    }

}
