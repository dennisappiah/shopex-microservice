package com.shopex.customer.repository;

import com.shopex.customer.model.PortfolioItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {

    Flux<PortfolioItem> findAllByCustomerId(Integer id);
}
