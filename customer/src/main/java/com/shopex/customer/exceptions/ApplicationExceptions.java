package com.shopex.customer.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

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
