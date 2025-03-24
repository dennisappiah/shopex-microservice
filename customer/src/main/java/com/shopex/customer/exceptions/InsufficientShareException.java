package com.shopex.customer.exceptions;

public class InsufficientShareException extends  RuntimeException{
    private static final String MESSAGE = "Customer [id=%d] does not have enough shares to complete transaction";

    public InsufficientShareException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
