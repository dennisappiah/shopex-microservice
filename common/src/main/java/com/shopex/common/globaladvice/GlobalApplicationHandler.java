package com.shopex.common.globaladvice;

import com.shopex.common.exceptions.CustomerNotFoundException;
import com.shopex.common.exceptions.InSufficientBalanceException;
import com.shopex.common.exceptions.InsufficientShareException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class GlobalApplicationHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("http://localhost:8080/api/problem-details/customer-not-found"));
        problem.setTitle("Customer Not Found");

        return problem;
    }

    @ExceptionHandler(InSufficientBalanceException.class)
    public ProblemDetail handleException(InSufficientBalanceException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("http://localhost:8080/api/problem-details/insufficient-balance"));
        problem.setTitle("Customer  does not have enough balance to complete transaction");

        return problem;
    }

    @ExceptionHandler(InsufficientShareException.class)
    public ProblemDetail handleException(InsufficientShareException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("http://localhost:8080/api/problem-details/insufficient-share"));
        problem.setTitle("Customer  does not have enough shares to complete transaction");

        return problem;
    }

}
