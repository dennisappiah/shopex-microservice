package com.shopex.common.exceptions;

import reactor.core.publisher.Mono;

import java.util.UUID;

public class GlobalExceptions {

    public static  <T> Mono<T> customerNotFound(UUID customerId){
        return Mono.error(new CustomerNotFoundException(customerId));
    }

    public static  <T> Mono<T> insufficientBalance(UUID customerId){
        return Mono.error(new InSufficientBalanceException((customerId)));
    }


    public static  <T> Mono<T> insufficientShares(UUID customerId){
        return Mono.error(new InsufficientShareException((customerId)));
    }


}
