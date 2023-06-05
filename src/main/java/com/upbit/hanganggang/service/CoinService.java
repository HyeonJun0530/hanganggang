package com.upbit.hanganggang.service;

import com.upbit.hanganggang.dto.CoinPriceResponseDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class CoinService {

    private final JSONParser jsonParser = new JSONParser();

    public Map<Integer, Object> createCoinList(List<String> JsonCoinList) throws ParseException {

        Map<Integer, Object> coinMap = new HashMap<>();
        List<CoinPriceResponseDto> coinList = new ArrayList<>();
        Integer count = 0;

        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        for (String coin : JsonCoinList) {

            coin = coin.replace("[", "").replace("]", "");

            JSONObject jsonObject = (JSONObject) jsonParser.parse(coin);

            String market = jsonObject.get("market").toString();
            Double tradePrice = (Double) jsonObject.get("trade_price");
            String jsonChange = jsonObject.get("change").toString();

            count = getCount(count, jsonChange);

            String change = changeStringValue(jsonChange);

            CoinPriceResponseDto coinPriceResponseDto = getCoinPriceResponseDto(decimalFormat, market, tradePrice, change);

            coinList.add(coinPriceResponseDto);
        }

        coinMap.put(0, coinList);
        coinMap.put(1, count);

        return coinMap;
    }

    private static Integer getCount(Integer count, String jsonChange) {
        if(jsonChange.contains("FALL")) {
            count--;
        } else if (jsonChange.contains("RISE")) {
            count++;
        }
        return count;
    }

    private static CoinPriceResponseDto getCoinPriceResponseDto(DecimalFormat decimalFormat, String market, Double tradePrice, String change) {
        CoinPriceResponseDto coinPriceResponseDto = new CoinPriceResponseDto();
        coinPriceResponseDto.setMarket(market);
        coinPriceResponseDto.setTradePrice(decimalFormat.format(tradePrice));
        coinPriceResponseDto.setChange(change);
        return coinPriceResponseDto;
    }

    private String changeStringValue(String change) {
        if (change.contains("FALL")) {
            change = "한강으로 풍덩~";
        } else if (change.contains("RISE")) {
            change = "한강 뷰로 가즈아~";
        } else {
            change = "횡보에 정신이 나가버릴거 같애~";
        }

        return change;
    }

}
