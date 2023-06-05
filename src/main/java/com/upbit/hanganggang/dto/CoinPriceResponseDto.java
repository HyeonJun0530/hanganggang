package com.upbit.hanganggang.dto;

import lombok.Data;

@Data
public class CoinPriceResponseDto {

    private String market;
    private String tradePrice;
    private String change;

}
