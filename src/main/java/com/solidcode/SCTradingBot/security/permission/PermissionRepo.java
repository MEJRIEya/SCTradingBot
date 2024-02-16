package com.solidcode.SCTradingBot.security.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Integer> {

    Permission findPermissionByPermissionId(Integer permissionId);
}
