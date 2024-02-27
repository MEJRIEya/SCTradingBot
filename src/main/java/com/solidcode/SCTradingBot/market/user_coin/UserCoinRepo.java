package com.solidcode.SCTradingBot.market.user_coin;

import com.solidcode.SCTradingBot.market.market_data_source.MarketDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCoinRepo extends JpaRepository<UserCoin, UserCoinId> {

    UserCoin findByUserCoinId(UserCoinId userCoinId);
}
