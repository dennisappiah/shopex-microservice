package com.shopex.customer.controller;

import com.shopex.common.dto.CustomerInformation;
import com.shopex.common.dto.StockTradeRequest;
import com.shopex.common.dto.StockTradeResponse;
import com.shopex.customer.dto.CreateCustomerRequest;
import com.shopex.customer.dto.CreateCustomerResponse;
import com.shopex.customer.service.CustomerService;
import com.shopex.customer.service.TradeService;
import com.shopex.customer.validators.RequestValidator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("api/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final TradeService tradeService;
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable UUID customerId) {
        log.info("Retrieving customer information for customerId: {}", customerId);

        return customerService.getCustomerInformation(customerId)
                .doOnSuccess(customerInfo -> log.debug("Successfully retrieved customer information: {}", customerInfo))
                .doOnError(error -> log.error("Error retrieving customer information for customerId: {}", customerId, error));
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(
            @PathVariable UUID customerId,
            @RequestBody Mono<StockTradeRequest> requestMono
    ) {
        return requestMono
                .flatMap(request -> {
                    log.info("Processing trade request for customerId: {}, Request: {}", customerId, request);

                    return tradeService.trade(customerId, request)
                            .doOnSuccess(response -> log.info("Trade completed successfully. CustomerId: {}, Response: {}",
                                    customerId, response))
                            .doOnError(error -> log.error("Trade failed for customerId: {}, Request: {}",
                                    customerId, request, error));
                });
    }


    @PostMapping
    public Mono<CreateCustomerResponse> saveCustomer(@RequestBody Mono<CreateCustomerRequest> mono) {
        return mono.transform(RequestValidator.validate())
                .doOnNext(request -> log.info("Received customer creation request: {}", request))
                .doOnError(validationError -> log.error("Validation failed for customer creation request", validationError))
                .flatMap(validatedRequest -> customerService.saveCustomer(Mono.just(validatedRequest)))
                .doOnNext(response -> log.info("Successfully created customer with ID: {}", response.id()))
                .onErrorResume(error -> {
                    log.error("Error during customer creation process", error);
                    return Mono.error(error);
                });
    }
}