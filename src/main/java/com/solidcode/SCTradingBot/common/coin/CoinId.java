package com.solidcode.SCTradingBot.common.coin;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinId implements Serializable {

    @Column(name = "market_data_source_id")
    private Integer marketDataSourceId;

    @Column(name = "coin_id")
    private String coinId;
}
