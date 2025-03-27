package com.shopex.common.exceptions;

import java.util.UUID;

public class InSufficientBalanceException extends RuntimeException{
    private static final String MESSAGE = "Customer [id=%d] does not have enough balance to complete transaction";

    public InSufficientBalanceException(UUID customerId) {
        super(MESSAGE.formatted(customerId));
    }
}
