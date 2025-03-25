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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TradeService {
    private final CustomerRepository customerRepository;

    private final PortfolioItemRepository portfolioItemRepository;


    @Transactional
    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest tradeRequest){
        return switch (tradeRequest.tradeAction()){
            case BUY -> this.buyStock(customerId, tradeRequest);
            case SELL -> this.sellStock(customerId, tradeRequest);
        };
    }

    private Mono<StockTradeResponse> buyStock(Integer customerId, StockTradeRequest tradeRequest){
       var customerMono = this.customerRepository.findById(customerId)
                .switchIfEmpty(GlobalExceptions.customerNotFound(customerId))
                // customer balance should be >= total price of stock
                .filter(customer ->  customer.getBalance() >= tradeRequest.totalPrice())
                .switchIfEmpty(GlobalExceptions.insufficientBalance(customerId));

       var portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, tradeRequest.ticker())
               .defaultIfEmpty(EntityDtoMapper.toPortfolioItemDto(customerId,tradeRequest.ticker() ));

       // as customer is found, subscribe to the portfolioItemMono publisher and execute buy
       return customerMono.zipWhen(customer -> portfolioItemMono)
                .flatMap(bothCustomerPortfolio ->
                        this.executeBuy(bothCustomerPortfolio.getT1(), bothCustomerPortfolio.getT2(), tradeRequest) );

    }


    private Mono<StockTradeResponse> executeBuy(
            Customer customer, PortfolioItem portfolioItem, StockTradeRequest tradeRequest){
        // deduct amount from customer balance
        //  add entry to portfolio if no records exists;
        //  increase by quantity if there is record
        customer.setBalance(customer.getBalance() - tradeRequest.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() + tradeRequest.quantity());
        var response = EntityDtoMapper.toStockTradeResponse(tradeRequest, customer.getId(), customer.getBalance());

      return  Mono.zip(this.customerRepository.save(customer), this.portfolioItemRepository.save(portfolioItem))
                .thenReturn(response);

    }


    private Mono<StockTradeResponse> sellStock(Integer customerId, StockTradeRequest tradeRequest){
        var customerMono = this.customerRepository.findById(customerId)
                .switchIfEmpty(GlobalExceptions.customerNotFound(customerId));

        var portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, tradeRequest.ticker())
                // check that portfolio quantity > request quantity before you can sell
                .filter(portfolioItem -> portfolioItem.getQuantity() > tradeRequest.quantity() )
                .switchIfEmpty(GlobalExceptions.insufficientShares(customerId));

        // as customer is found, subscribe to the portfolioItemMono publisher and execute sell
        return customerMono.zipWhen(customer -> portfolioItemMono)
                .flatMap(bothCustomerPortfolio ->
                        this.executeSell(bothCustomerPortfolio.getT1(), bothCustomerPortfolio.getT2(), tradeRequest) );

    }

    private Mono<StockTradeResponse> executeSell(
            Customer customer, PortfolioItem portfolioItem, StockTradeRequest tradeRequest){
        customer.setBalance(customer.getBalance() + tradeRequest.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() - tradeRequest.quantity());


        var response = EntityDtoMapper.toStockTradeResponse(tradeRequest, customer.getId(), customer.getBalance());

        return  Mono.zip(this.customerRepository.save(customer), this.portfolioItemRepository.save(portfolioItem))
                .thenReturn(response);
    }

}
