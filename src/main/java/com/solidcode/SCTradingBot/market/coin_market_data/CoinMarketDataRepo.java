package com.solidcode.SCTradingBot.market.coin_market_data;

import com.solidcode.SCTradingBot.market.market_data_source.MarketDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinMarketDataRepo extends JpaRepository<CoinMarketData, Integer> {
}
