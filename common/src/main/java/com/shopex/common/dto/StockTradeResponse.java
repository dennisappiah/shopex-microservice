package com.shopex.common.dto;


import com.shopex.common.domain.Ticker;
import com.shopex.common.domain.TradeAction;

import java.util.UUID;

public record StockTradeResponse (
    UUID customerId,
    Ticker ticker,
    Integer price,
    Integer quantity,
    TradeAction tradeAction,
    Integer totalPrice,
    Integer balance
){
}
