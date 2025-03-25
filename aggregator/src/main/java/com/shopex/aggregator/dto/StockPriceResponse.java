package com.shopex.aggregator.dto;

import com.shopex.common.domain.Ticker;

public record StockPriceResponse(
        Ticker ticker,
        Integer price
) {
}
