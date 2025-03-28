package com.shopex.stock.dto;

import com.shopex.common.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(
        Ticker ticker,
        Integer price,
        LocalDateTime time
) {
}
