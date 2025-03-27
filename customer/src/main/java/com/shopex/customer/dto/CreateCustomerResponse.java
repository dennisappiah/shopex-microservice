package com.shopex.customer.dto;

import java.util.UUID;

public record CreateCustomerResponse(
        UUID id,
        String name,
        Integer balance
) {
}
