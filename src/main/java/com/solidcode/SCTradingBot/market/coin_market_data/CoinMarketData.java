package com.solidcode.SCTradingBot.market.coin_market_data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coin_market_data")
public class CoinMarketData {

    @Id
    @EmbeddedId
    private CoinMarketDataId coinMarketDataId;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "ath")
    private BigDecimal ath;

    @Column(name = "ath_mc")
    private BigDecimal athMc;

    @Column(name = "circulating_supply")
    private BigDecimal circulatingSupply;

    @Column(name = "total_supply")
    private BigDecimal totalSupply;

    @Column(name = "max_supply")
    private BigDecimal maxSupply;
}
