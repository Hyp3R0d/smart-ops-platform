package com.smartops.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class JwtTokenUtil {

    private final JwtProperties properties;
    private final SecretKey secretKey;

    public JwtTokenUtil(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(JwtUser jwtUser) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setIssuer(properties.getIssuer())
                .setSubject(jwtUser.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + properties.getExpireSeconds() * 1000L))
                .claim("uid", jwtUser.getUserId())
                .claim("perms", jwtUser.getPermissions())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @SuppressWarnings("unchecked")
    public JwtUser parseToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        Object perms = claims.get("perms");
        return JwtUser.builder()
                .userId(claims.get("uid", Number.class).longValue())
                .username(claims.getSubject())
                .permissions(perms instanceof List<?> ? (List<String>) perms : Collections.emptyList())
                .build();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}
