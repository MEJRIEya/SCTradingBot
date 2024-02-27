package com.solidcode.SCTradingBot.common.coin;

import com.solidcode.SCTradingBot.security.user.ApiUserPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("{marketDataSourceId}/coins")
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @GetMapping(value = {"/{coinId}"})
    public ResponseEntity<Coin> getCoin(@PathVariable Integer marketDataSourceId, @PathVariable String coinId) {
        try {
            return ResponseEntity.ok().body(coinService.getCoin(marketDataSourceId, coinId));
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @GetMapping(value = {"/find/{symbol}"})
    public ResponseEntity<List<Coin>> findCoin(@PathVariable Integer marketDataSourceId, @PathVariable String symbol) {
        try {
            return ResponseEntity.ok().body(coinService.findCoin(marketDataSourceId, symbol));
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PostMapping(value = {"/add"})
    public ResponseEntity<CoinPayload> add(@PathVariable Integer marketDataSourceId, @RequestBody CoinPayload coinPayload) {
        try {
            return ResponseEntity.ok().body(coinService.add(marketDataSourceId, coinPayload));
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<CoinPayload> update(@PathVariable Integer marketDataSourceId, @RequestBody CoinPayload coinPayload) {

        try {
            return ResponseEntity.ok().body(coinService.update(marketDataSourceId, coinPayload));
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @DeleteMapping("/{coinId}")
    public ResponseEntity<?> delete(@PathVariable Integer marketDataSourceId, @PathVariable String coinId) {
        try {
            coinService.delete(marketDataSourceId, coinId);
            return ResponseEntity.ok().build();
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
