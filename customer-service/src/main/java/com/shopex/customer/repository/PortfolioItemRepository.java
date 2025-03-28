package com.shopex.customer.repository;


import com.shopex.common.domain.Ticker;
import com.shopex.customer.model.PortfolioItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, UUID> {

    // find all portfolio items for customer id
    Flux<PortfolioItem> findAllByCustomerId(UUID customerId);

    // get portfolio item by customerId and ticker (GOOGLE, FACEBOOK..)
    @Query("SELECT * FROM portfolio_item WHERE customer_id = :customerId AND ticker = :ticker")
    Mono<PortfolioItem> findByCustomerIdAndTicker(@Param("customerId") UUID customerId,
                                                  @Param("ticker") Ticker ticker);
}
