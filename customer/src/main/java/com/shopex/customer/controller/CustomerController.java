package com.shopex.customer.controller;

import com.shopex.common.dto.CustomerInformation;
import com.shopex.common.dto.StockTradeRequest;
import com.shopex.common.dto.StockTradeResponse;
import com.shopex.customer.service.CustomerService;
import com.shopex.customer.service.TradeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

// logging

@Repository
@RequestMapping("api/customers")
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;
    private TradeService tradeService;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId){
        return customerService.getCustomerInformation(customerId);

    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<StockTradeRequest> requestMono){
        return requestMono.flatMap(request -> tradeService.trade(customerId, request));
    }
}
