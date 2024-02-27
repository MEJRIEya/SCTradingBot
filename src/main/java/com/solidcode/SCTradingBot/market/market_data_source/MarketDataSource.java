package com.solidcode.SCTradingBot.market.market_data_source;

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
@Table(name = "market_data_source")
public class MarketDataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_data_source_id")
    private Integer marketDataSourceId;

    @Column(name = "name")
    private String name;

    @Column(name = "api_base_url")
    private String apiBaseUrl;

    @Column(name = "api_key")
    private String apiKey;
}
