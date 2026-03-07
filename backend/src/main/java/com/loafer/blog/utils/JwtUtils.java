package com.loafer.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(Long userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Long.parseLong(claims.getSubject());
    }

    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}