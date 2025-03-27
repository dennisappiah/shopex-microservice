package com.shopex.common.exceptions;

import java.util.UUID;

public class CustomerNotFoundException extends  RuntimeException{
    private static final String MESSAGE = "Customer [id=%d] is not found";

    public CustomerNotFoundException(UUID customerId) {
        super(MESSAGE.formatted(customerId));
    }
}
