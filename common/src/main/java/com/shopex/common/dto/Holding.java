package com.shopex.common.dto;


import com.shopex.common.domain.Ticker;

public record Holding(
        Ticker ticker,
        Integer quantity
) {
}
