package com.shopex.customer.dto;

import com.shopex.customer.domain.Ticker;
import com.shopex.customer.domain.TradeAction;

public record StockTradeRequest(
        Ticker ticker,
        Integer price,
        Integer quantity,
        TradeAction tradeAction
) {
}
