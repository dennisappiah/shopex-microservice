package com.shopex.customer.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("customer")
@Data
public class Customer {
    @Id
    private UUID id;
    private String name;
    private Integer balance;

    @PersistenceConstructor
    public Customer(UUID id, String name, Integer balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Customer() {
        // Required for Spring Data
    }

    public static Customer create(String name, Integer balance) {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName(name);
        customer.setBalance(balance);
        return customer;
    }
}