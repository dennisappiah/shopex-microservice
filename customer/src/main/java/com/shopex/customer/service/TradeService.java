package com.shopex.customer.service;

import com.shopex.common.dto.StockTradeRequest;
import com.shopex.common.dto.StockTradeResponse;
import com.shopex.common.exceptions.GlobalExceptions;
import com.shopex.customer.mapper.EntityDtoMapper;
import com.shopex.customer.model.Customer;
import com.shopex.customer.model.PortfolioItem;
import com.shopex.customer.repository.CustomerRepository;
import com.shopex.customer.repository.PortfolioItemRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@AllArgsConstructor
public class TradeService {
    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;
    private static final Logger log = LoggerFactory.getLogger(TradeService.class);

    @Transactional
    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest tradeRequest) {
        log.info("Processing trade request: CustomerId={}, Action={}, Ticker={}, Quantity={}",
                customerId, tradeRequest.tradeAction(), tradeRequest.ticker(), tradeRequest.quantity());

        return switch (tradeRequest.tradeAction()) {
            case BUY -> this.buyStock(customerId, tradeRequest)
                    .doOnSuccess(response -> log.info("Buy trade completed successfully. CustomerId={}", customerId))
                    .doOnError(error -> log.error("Buy trade failed. CustomerId={}", customerId, error));
            case SELL -> this.sellStock(customerId, tradeRequest)
                    .doOnSuccess(response -> log.info("Sell trade completed successfully. CustomerId={}", customerId))
                    .doOnError(error -> log.error("Sell trade failed. CustomerId={}", customerId, error))
                    .publishOn(Schedulers.boundedElastic());

        };
    }

    private Mono<StockTradeResponse> buyStock(Integer customerId, StockTradeRequest tradeRequest) {
        log.debug("Initiating buy stock process. CustomerId={}, Ticker={}", customerId, tradeRequest.ticker());

        var customerMono = this.customerRepository.findById(customerId)
                .doOnNext(customer -> log.debug("Customer found: {}", customer))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Customer not found for buyStock. CustomerId={}", customerId);
                    return GlobalExceptions.customerNotFound(customerId);
                }))
                .filter(customer -> {
                    boolean sufficientBalance = customer.getBalance() >= tradeRequest.totalPrice();
                    if (!sufficientBalance) {
                        log.warn("Insufficient balance for buy. CustomerId={}, Balance={}, TotalPrice={}",
                                customerId, customer.getBalance(), tradeRequest.totalPrice());
                    }
                    return sufficientBalance;
                })
                .switchIfEmpty(Mono.defer(() -> GlobalExceptions.insufficientBalance(customerId)));

        var portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, tradeRequest.ticker())
                .doOnNext(item -> log.debug("Existing portfolio item found: {}", item))
                .defaultIfEmpty(EntityDtoMapper.toPortfolioItemDto(customerId, tradeRequest.ticker()));

        return customerMono.zipWhen(customer -> portfolioItemMono)
                .flatMap(tuple -> this.executeBuy(tuple.getT1(), tuple.getT2(), tradeRequest));
    }

    private Mono<StockTradeResponse> executeBuy(
            Customer customer, PortfolioItem portfolioItem, StockTradeRequest tradeRequest) {
        log.debug("Executing buy. CustomerId={}, Ticker={}, Quantity={}",
                customer.getId(), tradeRequest.ticker(), tradeRequest.quantity());

        customer.setBalance(customer.getBalance() - tradeRequest.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() + tradeRequest.quantity());

        var response = EntityDtoMapper.toStockTradeResponse(tradeRequest, customer.getId(), customer.getBalance());

        return Mono.zip(
                        this.customerRepository.save(customer),
                        this.portfolioItemRepository.save(portfolioItem)
                )
                .doOnError(error -> log.error("Error saving buy transaction. CustomerId={}", customer.getId(), error))
                .thenReturn(response);
    }

    private Mono<StockTradeResponse> sellStock(Integer customerId, StockTradeRequest tradeRequest) {
        log.debug("Initiating sell stock process. CustomerId={}, Ticker={}", customerId, tradeRequest.ticker());

        var customerMono = this.customerRepository.findById(customerId)
                .doOnNext(customer -> log.debug("Customer found: {}", customer))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Customer not found for sellStock. CustomerId={}", customerId);
                    return GlobalExceptions.customerNotFound(customerId);
                }));

        var portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, tradeRequest.ticker())
                .doOnNext(item -> log.debug("Portfolio item found: {}", item))
                .filter(portfolioItem -> {
                    boolean sufficientShares = portfolioItem.getQuantity() >= tradeRequest.quantity();
                    if (!sufficientShares) {
                        log.warn("Insufficient shares for sell. CustomerId={}, CurrentQuantity={}, RequestQuantity={}",
                                customerId, portfolioItem.getQuantity(), tradeRequest.quantity());
                    }
                    return sufficientShares;
                })
                .switchIfEmpty(Mono.defer(() -> GlobalExceptions.insufficientShares(customerId)));

        return customerMono.zipWhen(customer -> portfolioItemMono)
                .flatMap(tuple -> this.executeSell(tuple.getT1(), tuple.getT2(), tradeRequest));
    }

    private Mono<StockTradeResponse> executeSell(
            Customer customer, PortfolioItem portfolioItem, StockTradeRequest tradeRequest) {
        log.debug("Executing sell. CustomerId={}, Ticker={}, Quantity={}",
                customer.getId(), tradeRequest.ticker(), tradeRequest.quantity());

        customer.setBalance(customer.getBalance() + tradeRequest.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() - tradeRequest.quantity());

        var response = EntityDtoMapper.toStockTradeResponse(tradeRequest, customer.getId(), customer.getBalance());

        return Mono.zip(
                        this.customerRepository.save(customer),
                        this.portfolioItemRepository.save(portfolioItem)
                )
                .doOnError(error -> log.error("Error saving sell transaction. CustomerId={}", customer.getId(), error))
                .thenReturn(response);
    }
}