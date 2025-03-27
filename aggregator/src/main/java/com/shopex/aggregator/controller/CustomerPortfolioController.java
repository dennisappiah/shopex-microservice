package com.shopex.aggregator.controller;

import com.shopex.aggregator.dto.TradeRequest;
import com.shopex.aggregator.service.CustomerPortfolioService;
import com.shopex.aggregator.validator.RequestValidator;
import com.shopex.common.dto.CustomerInformation;
import com.shopex.common.dto.StockTradeResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;


@RestController
@RequestMapping("api/customers")
@AllArgsConstructor
public class CustomerPortfolioController {

    private final CustomerPortfolioService customerPortfolioService;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable UUID customerId) {
        return this.customerPortfolioService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable UUID customerId, @RequestBody Mono<TradeRequest> mono) {
        return mono.transform(RequestValidator.validate())
                .flatMap(req -> this.customerPortfolioService.trade(customerId, req));
    }
}
