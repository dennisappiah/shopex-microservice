package com.shopex.customer.exceptions;

import reactor.core.publisher.Mono;

public class CustomerApplicationExceptions {
    public static <T> Mono<T> missingName(){
        return Mono.error(new InvalidInputException("Name is required"));
    }

    public static <T> Mono<T> missingBalance(){
        return Mono.error(new InvalidInputException("Balance is required"));
    }
}
