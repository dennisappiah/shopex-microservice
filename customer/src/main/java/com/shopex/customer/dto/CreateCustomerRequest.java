package com.shopex.customer.dto;

public record CreateCustomerRequest(
        Integer id,
        String name,
        Integer balance
) {
}
