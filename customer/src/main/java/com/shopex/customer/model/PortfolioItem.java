package com.shopex.customer.model;


import com.shopex.common.domain.Ticker;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Table("portfolio_item")
@Data
public class PortfolioItem {
    private Integer id;
    private Integer customerId;
    private Ticker ticker;
    private Integer quantity;
}
