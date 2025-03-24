package com.shopex.customer.service;

import com.shopex.customer.dto.CustomerInformation;
import com.shopex.customer.exceptions.ApplicationExceptions;
import com.shopex.customer.mapper.EntityDtoMapper;
import com.shopex.customer.model.Customer;
import com.shopex.customer.repository.CustomerRepository;
import com.shopex.customer.repository.PortfolioItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final PortfolioItemRepository portfolioItemRepository;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId){
        return this.customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .flatMap(this::buildCustomerInformation);

    }


    private Mono<CustomerInformation> buildCustomerInformation(Customer customer){
        return this.portfolioItemRepository.findAllByCustomerId(customer.getId())
                .collectList()
                .map(portfolioItems ->  EntityDtoMapper.toCustomerInformationDto(customer, portfolioItems));
    }
}
