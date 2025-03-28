package com.shopex.common.dto;



import com.shopex.common.domain.Ticker;
import com.shopex.common.domain.TradeAction;

public record TradeRequest(
        Ticker ticker,
        Integer price,
        Integer quantity,
        TradeAction tradeAction
) {
}