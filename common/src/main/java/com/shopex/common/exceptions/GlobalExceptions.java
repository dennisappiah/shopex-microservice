package com.shopex.common.exceptions;

import reactor.core.publisher.Mono;

public class GlobalExceptions {

    public static  <T> Mono<T> customerNotFound(Integer customerId){
        return Mono.error(new CustomerNotFoundException(customerId));
    }

    public static  <T> Mono<T> insufficientBalance(Integer customerId){
        return Mono.error(new InSufficientBalanceException((customerId)));
    }


    public static  <T> Mono<T> insufficientShares(Integer customerId){
        return Mono.error(new InsufficientShareException((customerId)));
    }


}
