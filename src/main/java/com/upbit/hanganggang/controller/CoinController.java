package com.upbit.hanganggang.controller;

import com.upbit.hanganggang.constant.ApiUrlEnum;
import com.upbit.hanganggang.dto.CoinPriceResponseDto;
import com.upbit.hanganggang.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class CoinController {

    private final CoinService coinService;
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/coin-price")
    public String getCoinPrice(Model model) throws ParseException {

        List<String> jsonCoinList = new ArrayList<>();

        List<ApiUrlEnum> apiUrlEnumList = List.of(ApiUrlEnum.values());

        for (int i = 0; i < apiUrlEnumList.size(); i++) {
            String apiUrl = "https://api.upbit.com/v1/ticker?markets=" + apiUrlEnumList.get(i).getToken();
            RequestEntity<Void> req = RequestEntity
                    .get(apiUrl)
                    .build();

            ResponseEntity<String> result = restTemplate.exchange(req,String.class);
            String coin = result.getBody();
            jsonCoinList.add(coin);
        }


        Map<Integer, Object> coinMap = coinService.createCoinList(jsonCoinList);
        List<CoinPriceResponseDto> coinList = (List<CoinPriceResponseDto>) coinMap.get(0);
        Integer count = (Integer) coinMap.get(1);
        model.addAttribute("coinList",coinList);
        model.addAttribute("count", count);

        return "coin";
    }
}
