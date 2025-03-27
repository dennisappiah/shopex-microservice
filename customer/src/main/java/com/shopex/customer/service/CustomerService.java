package com.shopex.customer.service;

import com.shopex.common.dto.CustomerInformation;
import com.shopex.common.exceptions.GlobalExceptions;
import com.shopex.customer.dto.CreateCustomerRequest;
import com.shopex.customer.dto.CreateCustomerResponse;
import com.shopex.customer.mapper.EntityDtoMapper;
import com.shopex.customer.model.Customer;
import com.shopex.customer.repository.CustomerRepository;
import com.shopex.customer.repository.PortfolioItemRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    public Mono<CustomerInformation> getCustomerInformation(UUID customerId) {
        log.info("Fetching customer information for customerId: {}", customerId);

        return this.customerRepository.findById(customerId)
                .doOnNext(customer -> log.debug("Customer found: {}", customer))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Customer not found with ID: {}", customerId);
                    return GlobalExceptions.customerNotFound(customerId);
                }))
                .flatMap(this::buildCustomerInformation)
                .doOnSuccess(customerInfo -> log.info("Successfully retrieved customer information for customerId: {}", customerId))
                .doOnError(error -> log.error("Error retrieving customer information for customerId: {}", customerId, error))
                .publishOn(Schedulers.boundedElastic());
    }

    private Mono<CustomerInformation> buildCustomerInformation(Customer customer) {
        log.debug("Building customer information for customer: {}", customer);

        return this.portfolioItemRepository.findAllByCustomerId(customer.getId())
                .collectList()
                .map(portfolioItems -> {
                    log.debug("Found {} portfolio items for customerId: {}", portfolioItems.size(), customer.getId());
                    return EntityDtoMapper.toCustomerInformationDto(customer, portfolioItems);
                })
                .doOnNext(customerInfo -> log.debug("Generated CustomerInformation: {}", customerInfo))
                .doOnError(error -> log.error("Error building customer information for customerId: {}", customer.getId(), error));
    }

    public Mono<CreateCustomerResponse> saveCustomer(Mono<CreateCustomerRequest> mono) {
        return mono
                .map(EntityDtoMapper::toEntity)
                .flatMap(this.customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }
}