package com.shopex.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
public class CustomerServiceApplication {
    public static void main(String[] args) {
        System.out.println(SpringApplication.run(CustomerServiceApplication.class));
    }
}
