package com.solidcode.SCTradingBot.market.user_coin;

import com.solidcode.SCTradingBot.common.coin.CoinId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_coin")
public class UserCoin {

    @Id
    @EmbeddedId
    private UserCoinId userCoinId;

    @Column(name = "display")
    private Boolean display;
}
