package com.solidcode.SCTradingBot.market.coin_sync;

import com.solidcode.SCTradingBot.common.api_client.ApiClient;
import com.solidcode.SCTradingBot.common.coin.Coin;
import com.solidcode.SCTradingBot.common.coin.CoinRepo;
import com.solidcode.SCTradingBot.common.api_client.coinranking.CoinRankingClient;
import com.solidcode.SCTradingBot.market.coin_market_data.CoinMarketData;
import com.solidcode.SCTradingBot.market.coin_market_data.CoinMarketDataId;
import com.solidcode.SCTradingBot.market.coin_market_data.CoinMarketDataRepo;
import com.solidcode.SCTradingBot.market.market_data_source.MarketDataSource;
import com.solidcode.SCTradingBot.market.market_data_source.MarketDataSourceRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class CoinSyncService {

    private final CoinRepo coinRepo;
    private final MarketDataSourceRepo marketDataSourceRepo;
    private final CoinMarketDataRepo coinMarketDataRepo;

    private ApiClient initApiClient(MarketDataSource marketDataSource) {
        ApiClient apiClient = null;
        switch(marketDataSource.getMarketDataSourceId()) {
            case 1: {
                apiClient = new CoinRankingClient(marketDataSource.getApiBaseUrl(), marketDataSource.getApiKey());
            }
            break;
            case 2: {

            }
        }
        return apiClient;
    }

    public void syncCoins(Integer marketDataSourceId) {

        MarketDataSource marketDataSource = marketDataSourceRepo.findByMarketDataSourceId(marketDataSourceId);
        if(marketDataSource == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Market Data Source does not exist.");
        }
        List<Coin> coinList = coinRepo.findAll();

        coinList.forEach(coin -> { syncCoin(marketDataSource, coin); });
    }

    public void syncCoin(MarketDataSource marketDataSource, Coin coin) {

        ApiClient apiClient = initApiClient(marketDataSource);
        coinMarketDataRepo.save(apiClient.getCoinData(coin));
    }

    public List<Coin> findCoin(MarketDataSource marketDataSource, String symbol) {

        ApiClient apiClient = initApiClient(marketDataSource);

        return apiClient.findCoin(symbol);
    }
}
