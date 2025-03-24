package com.shopex.customer.dto;

import com.shopex.customer.domain.Ticker;
import com.shopex.customer.domain.TradeAction;

public record StockTradeResponse (
    Integer customerId,
    Ticker ticker,
    Integer price,
    TradeAction tradeAction,
    Integer totalPrice,
    Integer balance
){
}
