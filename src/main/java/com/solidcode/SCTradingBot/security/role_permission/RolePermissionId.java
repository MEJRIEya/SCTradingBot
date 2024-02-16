package com.solidcode.SCTradingBot.security.role_permission;

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
public class RolePermissionId implements Serializable {

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "permission_id", nullable = false)
    private Integer permissionId;
}
