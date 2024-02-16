package com.solidcode.SCTradingBot.security.permission;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id", nullable = false)
    private Integer permissionId;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "http_method", length = 10)
    private String httpMethod;

    @Column(name = "api_path", length = 200)
    private String path;

    @Column(name = "status")
    private String status;

}
