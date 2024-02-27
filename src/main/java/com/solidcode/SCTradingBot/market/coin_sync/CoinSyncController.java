package com.solidcode.SCTradingBot.market.coin_sync;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/market_data")
@RequiredArgsConstructor
public class CoinSyncController {

    private final CoinSyncService coinSyncService;

    @GetMapping(value = {"/sync_coins"})
    public ResponseEntity<?> syncCoins(@RequestParam(value = "marketDataSourceId", defaultValue = "1") Integer marketDataSourceId) {

        try {
            coinSyncService.syncCoins(marketDataSourceId);
            return ResponseEntity.ok().build();
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
