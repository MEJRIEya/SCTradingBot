package com.solidcode.SCTradingBot.common.api_client;

import com.solidcode.SCTradingBot.common.coin.Coin;
import com.solidcode.SCTradingBot.market.coin_market_data.CoinMarketData;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public interface ApiClient {

    public WebClient build();
    public String getToken();
    public String apiCall(String endpoint, Boolean isPublic);
    public CoinMarketData getCoinData(Coin coin);
    public List<Coin> findCoin(String symbol);
}
