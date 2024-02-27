package com.solidcode.SCTradingBot.common.coin;

import com.solidcode.SCTradingBot.market.coin_sync.CoinSyncService;
import com.solidcode.SCTradingBot.market.market_data_source.MarketDataSource;
import com.solidcode.SCTradingBot.market.market_data_source.MarketDataSourceRepo;
import com.solidcode.SCTradingBot.market.user_coin.UserCoin;
import com.solidcode.SCTradingBot.market.user_coin.UserCoinId;
import com.solidcode.SCTradingBot.market.user_coin.UserCoinRepo;
import com.solidcode.SCTradingBot.security.user.User;
import com.solidcode.SCTradingBot.security.user.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class CoinService {

    private final CoinRepo coinRepo;
    private final UserService userService;
    private final UserCoinRepo userCoinRepo;
    private final MarketDataSourceRepo marketDataSourceRepo;
    private final CoinSyncService coinSyncService;

    public Coin getCoin(Integer marketDataSourceId, String coinId) {

        Coin coin = coinRepo.getCoinByCoinId(new CoinId(marketDataSourceId, coinId));
        if(coin == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Coin does not exist.");
        }
        return coin;
    }

    public List<Coin> findCoin(Integer marketDataSourceId, String symbol) {

        MarketDataSource marketDataSource = marketDataSourceRepo.findByMarketDataSourceId(marketDataSourceId);
        if(marketDataSource == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Market Data Source does not exist.");
        }

        return coinSyncService.findCoin(marketDataSource, symbol);
    }

    public CoinPayload add(Integer marketDataSourceId, CoinPayload coinPayload) {

        MarketDataSource marketDataSource = marketDataSourceRepo.findByMarketDataSourceId(marketDataSourceId);
        if(marketDataSource == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Market Data Source does not exist.");
        }

        Coin coin = coinRepo.getCoinByCoinId(new CoinId(marketDataSource.getMarketDataSourceId(), coinPayload.getCoinId()));
        if(coin == null) {
            coin = new Coin(new CoinId(marketDataSourceId, coinPayload.getCoinId()),
                    coinPayload.getSymbol(),
                    coinPayload.getName(),
                    coinPayload.getIconUrl(),
                    coinPayload.getCoinUrl());
            coinRepo.save(coin);
            coinSyncService.syncCoin(marketDataSource, coin);
        }

        User user = userService.getAuthenticatedUser();
        userCoinRepo.save(new UserCoin(new UserCoinId(user.getUserId(), marketDataSourceId, coin.getCoinId().getCoinId()), true));
        return coinPayload;
    }

    public CoinPayload update(Integer marketDataSourceId, CoinPayload coinPayload) {

        Coin coin = coinRepo.getCoinByCoinId(new CoinId(marketDataSourceId, coinPayload.getCoinId()));
        if(coin == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Coin does not exist.");
        }

        User user = userService.getAuthenticatedUser();
        UserCoin userCoin = userCoinRepo.findByUserCoinId(new UserCoinId(user.getUserId(), marketDataSourceId, coin.getCoinId().getCoinId()));
        userCoin.setDisplay(coinPayload.getDisplay());
        userCoinRepo.save(userCoin);
        return coinPayload;
    }

    public void delete(Integer marketDataSourceId, String coinId) {

        Coin coin = coinRepo.getCoinByCoinId(new CoinId(marketDataSourceId, coinId));
        if(coin == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Coin does not exist.");
        }

        User user = userService.getAuthenticatedUser();
        UserCoin userCoin = userCoinRepo.findByUserCoinId(new UserCoinId(user.getUserId(), marketDataSourceId, coin.getCoinId().getCoinId()));
        userCoinRepo.delete(userCoin);
    }
}
