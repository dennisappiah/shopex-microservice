package com.shopex.common.exceptions;

import java.util.UUID;

public class InsufficientShareException extends  RuntimeException{
    private static final String MESSAGE = "Customer [id=%d] does not have enough shares to complete transaction";

    public InsufficientShareException(UUID customerId) {
        super(MESSAGE.formatted(customerId));
    }
}
