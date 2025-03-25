package com.shopex.common.dto;

import java.util.List;

public record CustomerInformation (
        Integer id,
        String name,
        Integer balance,
        List<Holding> holdings
){

}
