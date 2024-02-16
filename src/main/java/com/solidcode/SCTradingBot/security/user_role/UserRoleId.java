package com.solidcode.SCTradingBot.security.user_role;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserRoleId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;
}
