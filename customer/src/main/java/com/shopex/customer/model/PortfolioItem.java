package com.shopex.customer.model;

import com.shopex.common.domain.Ticker;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("portfolio_item")
@Data
public class PortfolioItem {
    @Id
    private UUID id;

    @Column("customer_id")
    private UUID customerId;

    private Ticker ticker;
    private Integer quantity;

    @PersistenceConstructor
    public PortfolioItem(UUID id, UUID customerId, Ticker ticker, Integer quantity) {
        this.id = id;
        this.customerId = customerId;
        this.ticker = ticker;
        this.quantity = quantity;
    }

    public PortfolioItem() {

    }

    public static PortfolioItem create(UUID customerId, Ticker ticker, Integer quantity) {
        PortfolioItem item = new PortfolioItem();
        item.setId(UUID.randomUUID());
        item.setCustomerId(customerId);
        item.setTicker(ticker);
        item.setQuantity(quantity);
        return item;
    }
}