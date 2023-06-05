package com.upbit.hanganggang.constant;

import lombok.Getter;

@Getter
public enum ApiUrlEnum {
    BTC("KRW-BTC"), ETH("KRW-ETH"), DOGE("KRW-DOGE"), XRP("KRW-XRP"), ADA("KRW-ADA"),
    BCH("KRW-BCH"), XLM("KRW-XLM"), LINK("KRW-LINK");

    private String token;

    ApiUrlEnum(String token) {
        this.token = token;
    }
}
