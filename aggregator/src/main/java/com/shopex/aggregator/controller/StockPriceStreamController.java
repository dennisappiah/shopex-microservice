package com.shopex.aggregator.controller;


import com.shopex.aggregator.client.StockServiceClient;
import com.shopex.aggregator.dto.PriceUpdate;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("stock")
@AllArgsConstructor
public class StockPriceStreamController {

    private final StockServiceClient stockServiceClient;


    @GetMapping(value = "/price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PriceUpdate> priceUpdatesStream(){
        return this.stockServiceClient.priceUpdatesStream();
    }
}
