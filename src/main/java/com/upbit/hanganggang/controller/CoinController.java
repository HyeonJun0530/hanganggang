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

//롬복 사용
@RequiredArgsConstructor
@Controller
public class CoinController {

    private final CoinService coinService;
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/coin-price")
    public String getCoinPrice(Model model) throws ParseException {

        List<String> jsonCoinList = new ArrayList<>();

        List<ApiUrlEnum> apiUrlEnumList = List.of(ApiUrlEnum.values());

        //Upbit Api 호출을 통해 넘어온 코인에 대한 데이터를 Json으로 파싱
        for (int i = 0; i < apiUrlEnumList.size(); i++) {
            String apiUrl = "https://api.upbit.com/v1/ticker?markets=" + apiUrlEnumList.get(i).getToken();
            RequestEntity<Void> req = RequestEntity
                    .get(apiUrl)
                    .build();

            ResponseEntity<String> result = restTemplate.exchange(req,String.class);
            String coin = result.getBody();
            jsonCoinList.add(coin);
        }

        // 파싱된 데이터들을 받아서 View -> coin.html의 타임리프로 넘겨줌
        // count를 체크해서 리스트의 코인에 오른게 많은지 내린게 많은지 체크
        // Service에서 ResponseDto로 coin정보를 파싱함.
        Map<Integer, Object> coinMap = coinService.createCoinList(jsonCoinList);
        List<CoinPriceResponseDto> coinList = (List<CoinPriceResponseDto>) coinMap.get(0);
        Integer count = (Integer) coinMap.get(1);

        String api = "http://192.168.0.66:9000";
        RequestEntity<Void> req = RequestEntity
                .get(api)
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req,String.class);
        String shock = result.getBody();
        int shockNum = coinService.jsonToInt(shock);

        model.addAttribute("coinList",coinList);
        model.addAttribute("count", count);
        model.addAttribute("shock", shockNum);

        return "coin";
    }
}
