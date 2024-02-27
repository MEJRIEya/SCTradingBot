package com.solidcode.SCTradingBot.common.api_client.coinranking;

import com.solidcode.SCTradingBot.common.api_client.ApiClient;
import com.solidcode.SCTradingBot.common.coin.Coin;
import com.solidcode.SCTradingBot.common.coin.CoinId;
import com.solidcode.SCTradingBot.market.coin_market_data.CoinMarketData;
import com.solidcode.SCTradingBot.market.coin_market_data.CoinMarketDataId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoinRankingClient implements ApiClient {

    private String apiBaseURL;
    private WebClient webClient;
    private int maxCallTry;
    private int callTryCount;
    private String apiKey;

    public CoinRankingClient(String apiBaseURL, String apiKey) {
        this.apiBaseURL = apiBaseURL;
        this.apiKey = apiKey;
        this.maxCallTry = 10;
        this.callTryCount = 0;
        this.webClient = build();
    }

    @Override
    public WebClient build() {
        return WebClient.builder()
                .baseUrl(apiBaseURL)
                .exchangeStrategies(useMaxMemory())
                .filter(errorHandler())
                .build();
    }

    @Override
    public String getToken() {
        return apiKey;
    }

    @Override
    public String apiCall(String endpoint, Boolean isPublic) {
        try {
            StopWatch watch = new StopWatch();
            watch.start();
            HttpHeaders headers = new HttpHeaders();
            if(!isPublic) {
                headers.add("x-access-token",getToken());
            }
            headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            byte[] bytes = webClient.get()
                        .uri(endpoint)
                        .headers(h -> h.addAll(headers))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block();
            String response = new String(bytes, StandardCharsets.UTF_8);
            watch.stop();
            if(watch.getTotalTimeMillis() < 1000) {
                TimeUnit.MILLISECONDS.sleep(1002 - watch.getTotalTimeMillis());
            }
            //System.out.println(response);
            return response;
        } catch (Exception e) {
            callTryCount++;
            if(callTryCount < maxCallTry){
                try {
                    System.out.println("Call Try Count: "+callTryCount);
                    TimeUnit.SECONDS.sleep(60);
                    apiCall(endpoint, isPublic);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public static ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is5xxServerError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new Exception(errorBody)));
            } else if (clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new Exception(errorBody)));
            } else {
                return Mono.just(clientResponse);
            }
        });
    }

    @Override
    public CoinMarketData getCoinData(Coin coin) {

        String content = apiCall("/coin/"+coin.getCoinId().getCoinId(), false);
        JSONObject jResult = new JSONObject(content);
        JSONObject jCoin = jResult.getJSONObject("data").getJSONObject("coin");
        BigDecimal currentPrice = BigDecimal.valueOf(Double.parseDouble(jCoin.getString("price")));
        JSONObject jAth = jCoin.getJSONObject("allTimeHigh");
        BigDecimal ath = BigDecimal.valueOf(Double.parseDouble(jAth.getString("price")));
        JSONObject jSupply = jCoin.getJSONObject("supply");
        BigDecimal cs = BigDecimal.valueOf(Double.parseDouble(!jSupply.isNull("circulating")?jSupply.getString("circulating"):"0"));
        BigDecimal totalSupply = BigDecimal.valueOf(Double.parseDouble(!jSupply.isNull("total")?jSupply.getString("total"):"0"));
        BigDecimal maxSupply = BigDecimal.valueOf(Double.parseDouble(!jSupply.isNull("max")?jSupply.getString("max"):"0"));

        return new CoinMarketData(new CoinMarketDataId(1, coin.getCoinId().getCoinId(), new Date()), currentPrice, ath, new BigDecimal(0),
                cs, totalSupply, maxSupply);
    }

    @Override
    public List<Coin> findCoin(String symbol) {
        String content = apiCall("/coins/?symbols="+symbol, true);
        JSONArray jCoins = (new JSONObject(content)).getJSONObject("data").getJSONArray("coins");
        List<Coin> coinList = new ArrayList<>();
        jCoins.forEach(item -> {
            JSONObject jCoin = (JSONObject) item;
            coinList.add(new Coin(new CoinId(1, jCoin.getString("uuid")),
                    jCoin.getString("symbol"),
                    jCoin.getString("name"),
                    jCoin.getString("iconUrl"),
                    jCoin.getString("coinrankingUrl")));
        });
        return coinList;
    }

    private static ExchangeStrategies useMaxMemory() {
        long totalMemory = Runtime.getRuntime().maxMemory();

        return ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize((int) totalMemory)
                )
                .build();
    }
}
