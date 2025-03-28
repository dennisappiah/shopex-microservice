package com.shopex.customer.dto;


public record CreateCustomerRequest(
        String name,
        Integer balance
) {
}
