package com.shopex.aggregator.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient stockServiceWebClient(
            @Value("${services.stock.base-url}") String baseUrl,
            WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public WebClient customerServiceWebClient(
            @Value("${services.customer.base-url}") String baseUrl,
            WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }
}