package com.solidcode.SCTradingBot.security.role_permission;

import com.solidcode.SCTradingBot.security.permission.Permission;
import com.solidcode.SCTradingBot.security.role.Role;
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
@Table(name = "role_permission")
public class RolePermission {
    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
    })
    private Role role;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "permission_id", referencedColumnName = "permission_id", insertable = false, updatable = false)
    })
    private Permission permission;
}
