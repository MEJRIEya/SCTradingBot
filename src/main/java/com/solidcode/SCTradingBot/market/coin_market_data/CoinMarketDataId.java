package com.solidcode.SCTradingBot.market.coin_market_data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinMarketDataId implements Serializable {

    @Column(name = "market_data_source_id")
    private Integer marketDataSourceId;

    @Column(name = "coin_id")
    private String coinId;

    @Column(name = "sync_date")
    private Date syncDate;
}
