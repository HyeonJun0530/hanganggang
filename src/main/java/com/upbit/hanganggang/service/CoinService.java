package com.upbit.hanganggang.service;

import com.upbit.hanganggang.dto.CoinPriceResponseDto;
import com.upbit.hanganggang.dto.CoinRaspiResponseDto;
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
            String jsonChangeRate = jsonObject.get("change_rate").toString();

            count = getCount(count, jsonChange);

            String change = changeStringValue(jsonChange);

            Double changeRate = changeRateDisplay(jsonChange, jsonChangeRate);

            CoinPriceResponseDto coinPriceResponseDto = getCoinPriceResponseDto(decimalFormat, market,
                    tradePrice, change, changeRate);

            coinList.add(coinPriceResponseDto);
        }

        coinMap.put(0, coinList);
        coinMap.put(1, count);

        return coinMap;
    }

    public List<CoinRaspiResponseDto> raspiCoinList(List<String> JsonCoinList) throws ParseException {

        List<CoinRaspiResponseDto> coinList = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        for (String coin : JsonCoinList) {

            coin = coin.replace("[", "").replace("]", "");

            JSONObject jsonObject = (JSONObject) jsonParser.parse(coin);

            String market = jsonObject.get("market").toString();
            Double tradePrice = (Double) jsonObject.get("trade_price");
            String jsonChange = jsonObject.get("change").toString();

            CoinRaspiResponseDto coinRaspiResponseDto = getCoinPriceResponseDto(decimalFormat, market,
                    tradePrice, jsonChange);

            coinList.add(coinRaspiResponseDto);
        }


        return coinList;
    }

    private Double changeRateDisplay(String jsonChange, String jsonChangeRate) {
        double changeRate = Double.parseDouble(jsonChangeRate);
        if (jsonChange.contains("FALL")) {
            changeRate *= -1;
        }

        // 소수점 둘째 자리까지 반올림
        changeRate = Math.round(changeRate * 100.0) / 100.0;

        return changeRate;
    }

    private static Integer getCount(Integer count, String jsonChange) {
        if (jsonChange.contains("FALL")) {
            count--;
        } else if (jsonChange.contains("RISE")) {
            count++;
        }
        return count;
    }

    private static CoinPriceResponseDto getCoinPriceResponseDto(DecimalFormat decimalFormat, String market,
                                                                Double tradePrice, String change,
                                                                Double changeRate) {
        CoinPriceResponseDto coinPriceResponseDto = new CoinPriceResponseDto();
        coinPriceResponseDto.setMarket(market);
        coinPriceResponseDto.setTradePrice(decimalFormat.format(tradePrice));
        coinPriceResponseDto.setChange(change);
        coinPriceResponseDto.setChangeRate(changeRate);
        return coinPriceResponseDto;
    }

    private static CoinRaspiResponseDto getCoinPriceResponseDto(DecimalFormat decimalFormat, String market,
                                                                Double tradePrice, String change) {
        CoinRaspiResponseDto coinRaspiResponseDto = new CoinRaspiResponseDto();
        coinRaspiResponseDto.setMarket(market);
        coinRaspiResponseDto.setTradePrice(decimalFormat.format(tradePrice));
        coinRaspiResponseDto.setChange(change);

        return coinRaspiResponseDto;
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
