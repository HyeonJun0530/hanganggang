package com.upbit.hanganggang.constant;

import lombok.Getter;

/**
 * Api 호출 시 업비트 서버로 부터 받을 코인 목록을 Enum으로 정의함
 */
@Getter
public enum ApiUrlEnum {
    BTC("KRW-BTC"), ETH("KRW-ETH"), DOGE("KRW-DOGE"), XRP("KRW-XRP"), ADA("KRW-ADA"),
    BCH("KRW-BCH"), XLM("KRW-XLM"), LINK("KRW-LINK"), STRIKE("KRW-STRK");

    private String token;

    ApiUrlEnum(String token) {
        this.token = token;
    }
}
