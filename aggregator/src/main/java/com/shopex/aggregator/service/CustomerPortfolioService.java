package com.shopex.aggregator.service;


import com.shopex.aggregator.client.CustomerServiceClient;
import com.shopex.aggregator.client.StockServiceClient;
import com.shopex.aggregator.dto.StockPriceResponse;
import com.shopex.aggregator.dto.TradeRequest;
import com.shopex.common.dto.CustomerInformation;
import com.shopex.common.dto.StockTradeRequest;
import com.shopex.common.dto.StockTradeResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CustomerPortfolioService {

    private final StockServiceClient stockServiceClient;
    private final CustomerServiceClient customerServiceClient;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.customerServiceClient.getCustomerInformation(customerId);
    }

    public Mono<StockTradeResponse> trade(Integer customerId, TradeRequest request) {
        return this.stockServiceClient.getStockPrice(request.ticker())
                .map(StockPriceResponse::price)
                .map(price -> this.toStockTradeRequest(request, price))
                .flatMap(req -> this.customerServiceClient.trade(customerId, req));
    }

    private StockTradeRequest toStockTradeRequest(TradeRequest request, Integer price) {
        return new StockTradeRequest(
                request.ticker(),
                price,
                request.quantity(),
                request.tradeAction()
        );


    }
}
