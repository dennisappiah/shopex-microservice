package com.shopex.customer.validators;

import com.shopex.customer.dto.CreateCustomerRequest;
import com.shopex.customer.exceptions.CustomerApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {
    public static UnaryOperator<Mono<CreateCustomerRequest>> validate(){
        return mono -> mono.filter(hasName())
                .switchIfEmpty(CustomerApplicationExceptions.missingName())
                .filter(hasBalance())
                .switchIfEmpty(CustomerApplicationExceptions.missingBalance());
    }

    public static Predicate<CreateCustomerRequest> hasName(){
        return customerRequest -> Objects.nonNull(customerRequest.name());
    }


    public static Predicate<CreateCustomerRequest> hasBalance(){
        return customerRequest -> Objects.nonNull(customerRequest.balance());

    }
}
