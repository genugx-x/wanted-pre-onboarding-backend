package com.genug.wpob.security;

import io.jsonwebtoken.Jwts;
import lombok.Builder;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TokenProvider {

    private final Key secretKey;

    @Builder
    public TokenProvider(Key secretKey) {
        this.secretKey = secretKey;
    }

    // token 생성
    public String create(String subject) {
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        return Jwts.builder()
                .signWith(secretKey)
                .setSubject(subject)
                .setIssuer("WPOB")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    // token 검증
    public String validateAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
