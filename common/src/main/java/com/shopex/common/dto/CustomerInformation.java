package com.shopex.common.dto;

import java.util.List;
import java.util.UUID;


public record CustomerInformation (
        UUID id,
        String name,
        Integer balance,
        List<Holding> holdings
){

}


