package com.solidcode.SCTradingBot.security.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepo permissionRepo;

    public Permission getPermission(Integer permissionId) throws DataAccessException {
        Permission permission = permissionRepo.findPermissionByPermissionId(permissionId);
        if (permission == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Permission not found.");
        }
        return permission;
    }

    public List<Permission> getAllPermission() throws DataAccessException {

        return permissionRepo.findAll();
    }
}
