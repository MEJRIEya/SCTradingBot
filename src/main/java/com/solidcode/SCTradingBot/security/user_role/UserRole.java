package com.solidcode.SCTradingBot.security.user_role;

import com.solidcode.SCTradingBot.security.role.Role;
import com.solidcode.SCTradingBot.security.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_role")
public class UserRole {
    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    })
    private User user;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
    })
    private Role role;
}
