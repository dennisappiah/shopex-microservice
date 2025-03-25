package com.shopex.customer.repository;


import com.shopex.common.domain.Ticker;
import com.shopex.customer.model.PortfolioItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {

    // find all portfolio items for customer id
    Flux<PortfolioItem> findAllByCustomerId(Integer customerId);

    // get portfolio item by customerId and ticker (GOOGLE, FACEBOOK..)
    Mono<PortfolioItem> findByCustomerIdAndTicker(Integer customerId, Ticker ticker);
}
