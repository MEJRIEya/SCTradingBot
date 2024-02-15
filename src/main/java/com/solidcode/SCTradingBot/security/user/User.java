package com.solidcode.SCTradingBot.security.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "complete_name")
    private String completeName;
    @Column(name = "status")
    private String status;

    public User(String username, String password, String completeName, String status) {
        this.username = username;
        this.password = password;
        this.completeName = completeName;
        this.status = status;
    }
}
