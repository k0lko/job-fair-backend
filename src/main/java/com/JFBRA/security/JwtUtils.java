package com.JFBRA.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ================= TOKEN GENERATION =================

    public String generateToken(String email, String role) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    // ================= TOKEN VALIDATION =================

    public boolean validateToken(String jwt) {
        try {
            parseClaims(jwt);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // ================= CLAIMS EXTRACTION =================

    public String getEmailFromToken(String jwt) {
        return parseClaims(jwt).getSubject();
    }

    public String getRoleFromToken(String jwt) {
        return parseClaims(jwt).get("role", String.class);
    }

    // ================= INTERNAL =================

    private Claims parseClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
