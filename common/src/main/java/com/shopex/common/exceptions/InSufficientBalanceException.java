package com.shopex.common.exceptions;

public class InSufficientBalanceException extends RuntimeException{
    private static final String MESSAGE = "Customer [id=%d] does not have enough balance to complete transaction";

    public InSufficientBalanceException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
