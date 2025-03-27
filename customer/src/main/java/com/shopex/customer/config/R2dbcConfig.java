package com.shopex.customer.config;

import com.shopex.customer.model.Customer;
import com.shopex.customer.model.PortfolioItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Configuration
public class R2dbcConfig {

    @Bean
    public BeforeConvertCallback<Customer> customerIdGenerator() {
        return (customer, table) -> {
            if (customer.getId() == null) {
                customer.setId(UUID.randomUUID());
            }
            return Mono.just(customer);
        };
    }

    @Bean
    public BeforeConvertCallback<PortfolioItem> portfolioItemIdGenerator() {
        return (item, table) -> {
            if (item.getId() == null) {
                item.setId(UUID.randomUUID());
            }
            return Mono.just(item);
        };
    }
}