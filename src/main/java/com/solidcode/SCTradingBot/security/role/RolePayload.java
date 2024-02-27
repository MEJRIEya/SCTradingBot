package com.solidcode.SCTradingBot.security.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RolePayload {

    private Integer roleId;
    private String name;
}
