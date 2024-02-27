package com.solidcode.SCTradingBot.market.user_coin;

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
public class UserCoinId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "market_data_source_id")
    private Integer marketDataSourceId;

    @Column(name = "coin_id")
    private String coinId;
}
