package com.solidcode.SCTradingBot.market.market_data_source;

import com.solidcode.SCTradingBot.common.coin.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketDataSourceRepo extends JpaRepository<MarketDataSource, Integer> {

    MarketDataSource findByMarketDataSourceId(Integer marketDataSourceId);
}
