package com.shopex.customer.repository;

import com.shopex.customer.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
