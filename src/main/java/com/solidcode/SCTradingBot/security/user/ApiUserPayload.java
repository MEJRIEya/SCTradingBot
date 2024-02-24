package com.solidcode.SCTradingBot.security.user;

import com.solidcode.SCTradingBot.security.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiUserPayload {

    private Integer userId;
    private String username;
    private String password;
    private String confirmPassword;
    private String completeName;
    private String status;
    private List<Role> roles;
}
