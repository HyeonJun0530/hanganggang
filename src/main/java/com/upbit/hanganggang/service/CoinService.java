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

    /**
     * Json을 Dto로 파싱
     * @param JsonCoinList
     * @return
     * @throws ParseException
     */
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

    public int jsonToInt(String shock) throws ParseException {
        JSONObject jsonObject = (JSONObject) jsonParser.parse(shock);

        int shockNum = Integer.parseInt(jsonObject.get("shock").toString());

        return shockNum;
    }


    /**
     * 절대값으로 넘어온 등락률을 FALL, RISE로 넘어온 +/- 여부를 Double Data로 넘겨줌
     * @param jsonChange
     * @param jsonChangeRate
     * @return
     */
    private Double changeRateDisplay(String jsonChange, String jsonChangeRate) {
        double changeRate = Double.parseDouble(jsonChangeRate);
        // 소수점 둘째 자리까지 반올림
        changeRate = Math.round(changeRate * 10000.0) / 100.0;

        if (jsonChange.contains("FALL")) {
            changeRate *= -1;
        }

        return changeRate;
    }

    /**
     * 목록의 코인중 오르거나 내린 코인을 세어 총합을 반환해서 이미지 표시에 사용
     * @param count
     * @param jsonChange
     * @return
     */
    private static Integer getCount(Integer count, String jsonChange) {
        if (jsonChange.contains("FALL")) {
            count--;
        } else if (jsonChange.contains("RISE")) {
            count++;
        }
        return count;
    }

    /**
     * Dto 반환
     * @param decimalFormat
     * @param market
     * @param tradePrice
     * @param change
     * @param changeRate
     * @return
     */
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

    /**
     * change 정보에 따라 문자열을 넣어줌
     * @param change
     * @return
     */
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
