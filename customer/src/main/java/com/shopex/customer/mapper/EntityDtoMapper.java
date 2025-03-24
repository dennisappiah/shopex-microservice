package com.shopex.customer.mapper;

import com.shopex.customer.dto.CustomerInformation;
import com.shopex.customer.dto.Holding;
import com.shopex.customer.model.Customer;
import com.shopex.customer.model.PortfolioItem;

import java.util.List;

public class EntityDtoMapper {

    public static CustomerInformation toCustomerInformationDto
            (Customer customer, List<PortfolioItem> portfolioItems){

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



}
