package com.solidcode.SCTradingBot.common.coin;

import com.solidcode.SCTradingBot.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepo extends JpaRepository<Coin, CoinId> {

    Coin getCoinByCoinId(CoinId coinId);
}
