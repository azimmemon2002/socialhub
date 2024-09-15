package com.socialhub.auth.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.security.jwt.expiration}")
    private Long jwtExpirationInMs;

    @Getter
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates a JWT token based on the authenticated user.
     *
     * @param authentication the authentication object containing user details
     * @return the generated JWT token as a String
     */
    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User userPrincipal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String roles = userPrincipal.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from a given JWT token.
     *
     * @param token the JWT token
     * @return the username as a String
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /**
     * Extracts roles from a given JWT token.
     *
     * @param token the JWT token
     * @return roles as a comma-separated String
     */
    public String getRolesFromJWT(String token) {
        Claims claims = parseClaims(token);
        return claims.get("roles", String.class);
    }

    /**
     * Validates the JWT token's integrity and expiration.
     *
     * @param authToken the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String authToken) {
        try {
            parseClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // Log the exception if needed
            return false;
        }
    }

    /**
     * Parses the claims from the JWT token.
     *
     * @param token the JWT token
     * @return Claims extracted from the token
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(secretKey)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }
}
