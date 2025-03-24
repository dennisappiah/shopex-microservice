package com.shopex.customer.dto;

import com.shopex.customer.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {
}
