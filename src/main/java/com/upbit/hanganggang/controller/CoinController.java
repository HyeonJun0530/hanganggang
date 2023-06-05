package com.upbit.hanganggang.controller;

import com.upbit.hanganggang.dto.CoinPriceResponseDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;

@Controller
public class CoinController {
    @GetMapping("/btc-price")
    public String getCoinPrice(Model model) throws ParseException {

        JSONParser jsonParser = new JSONParser();
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        // API 요청을 보낼 URL
        String apiUrl = "https://api.upbit.com/v1/ticker?markets=KRW-BTC";

        // RestTemplate을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<Void> req = RequestEntity
                .get(apiUrl)
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req,String.class);
        String str = result.getBody();
        str = str.replace("[", "").replace("]", "");

        JSONObject jsonObject = (JSONObject) jsonParser.parse(str);

        String market = jsonObject.get("market").toString();
        Double tradePrice = (Double) jsonObject.get("trade_price");
        String change = jsonObject.get("change").toString();

        CoinPriceResponseDto coinPriceResponseDto = new CoinPriceResponseDto();
        coinPriceResponseDto.setMarket(market);
        coinPriceResponseDto.setTradePrice(decimalFormat.format(tradePrice));
        coinPriceResponseDto.setChange(change);

        model.addAttribute("coin",coinPriceResponseDto);

        return "btckrw";
    }
}
