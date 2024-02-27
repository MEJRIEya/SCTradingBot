package com.solidcode.SCTradingBot.common.coin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoinPayload {

    private String coinId;
    private String symbol;
    private String name;
    private String iconUrl;
    private String coinUrl;
    private Boolean display;
}
