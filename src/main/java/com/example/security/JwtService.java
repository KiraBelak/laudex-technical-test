package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import jakarta.annotation.PostConstruct;
import java.util.Base64;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private Key key;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    @PostConstruct
    public void init() {
        try {
            key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
            logger.info("JWT key initialized successfully. Save this key in your application.properties: {}",
                    encodedKey);
        } catch (Exception e) {
            logger.error("Error initializing JWT key: {}", e.getMessage());
            throw new IllegalStateException("Could not initialize JWT key", e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            String token = Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
            logger.debug("Generated token for user: {}", userDetails.getUsername());
            return token;
        } catch (Exception e) {
            logger.error("Error generating token: {}", e.getMessage());
            throw new JwtException("Could not generate token", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
            logger.debug("Token validation for user {}: {}", username, isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error validating token: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        boolean isExpired = expiration.before(new Date());
        logger.debug("Token expiration check - Expiration: {}, Is Expired: {}", expiration, isExpired);
        return isExpired;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.debug("Successfully extracted claims from token");
            return claims;
        } catch (Exception e) {
            logger.error("Error extracting claims from token: {}", e.getMessage());
            throw new JwtException("Could not extract claims from token", e);
        }
    }
}