package com.solidcode.SCTradingBot.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.solidcode.SCTradingBot.security.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Token {

    private final Algorithm algorithm;

    public Token() {
        this.algorithm = Algorithm.HMAC256("WO4L!OxechIyotoqabUp?i&EniCH7jLWEt=l0".getBytes());
    }

    public String createAccessToken(User user, String issuer) {
        List<String> permissions = new ArrayList<>();
        permissions.add("ALL");
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("permissions", permissions)
                .sign(algorithm);
    }

    public String createRefreshToken(User user, String issuer) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
