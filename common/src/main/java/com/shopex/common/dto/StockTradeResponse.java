package com.shopex.common.dto;


import com.shopex.common.domain.Ticker;
import com.shopex.common.domain.TradeAction;

public record StockTradeResponse (
    Integer customerId,
    Ticker ticker,
    Integer price,
    Integer quantity,
    TradeAction tradeAction,
    Integer totalPrice,
    Integer balance
){
}
