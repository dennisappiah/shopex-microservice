package com.shopex.customer.mapper;

import com.shopex.common.dto.CustomerInformation;
import com.shopex.common.dto.Holding;
import com.shopex.common.dto.StockTradeRequest;
import com.shopex.common.dto.StockTradeResponse;
import com.shopex.common.domain.Ticker;
import com.shopex.customer.dto.CreateCustomerRequest;
import com.shopex.customer.dto.CreateCustomerResponse;
import com.shopex.customer.model.Customer;
import com.shopex.customer.model.PortfolioItem;

import java.util.List;
import java.util.UUID;

public class EntityDtoMapper {

    public static CustomerInformation toCustomerInformationDto(
            Customer customer,
            List<PortfolioItem> portfolioItems
    ) {
        var holdings = portfolioItems
                .stream()
                .map(item -> new Holding(item.getTicker(), item.getQuantity()))
                .toList();

        return new CustomerInformation(
                customer.getId(),
                customer.getName(),
                customer.getBalance(),
                holdings
        );
    }


    public static PortfolioItem toPortfolioItemDto(UUID customerId, Ticker ticker){
       var portfolioItem = new PortfolioItem();
       portfolioItem.setCustomerId(customerId);
       portfolioItem.setTicker(ticker);
       portfolioItem.setQuantity(0);

       return portfolioItem;
    }

    // building the stock Trade Response
    public static StockTradeResponse toStockTradeResponse(StockTradeRequest stockTradeRequest,
                                                          UUID customerId, Integer balance ){
        return new StockTradeResponse(
                customerId,
                stockTradeRequest.ticker(),
                stockTradeRequest.price(),
                stockTradeRequest.quantity(),
                stockTradeRequest.tradeAction(),
                stockTradeRequest.totalPrice(),
                balance
        );
    }

    // maps customer request dto to customer entity
    public static Customer toEntity(CreateCustomerRequest customerDto) {
        var customer = new Customer();
        customer.setName(customerDto.name());
        customer.setBalance(customerDto.balance());
        return customer;
    }

    public static CreateCustomerResponse toDto(Customer customer) {
        return new CreateCustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getBalance()
        );
    }

}
