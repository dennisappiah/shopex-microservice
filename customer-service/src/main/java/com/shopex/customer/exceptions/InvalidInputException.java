package com.shopex.customer.exceptions;

public class InvalidInputException extends  RuntimeException{

    public InvalidInputException(String message) {
        super(message);
    }
}