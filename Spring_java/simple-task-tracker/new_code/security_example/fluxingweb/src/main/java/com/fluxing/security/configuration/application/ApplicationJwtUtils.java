package com.fluxing.security.configuration.application;

import com.fluxing.security.configuration.security.AppUserDetailsJwt;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class ApplicationJwtUtils {

    private final Logger log = LoggerFactory.getLogger(ApplicationJwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateJwtToken(AppUserDetailsJwt userDetailsJwt) {
        return generateTokenFromUsername(userDetailsJwt.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + (tokenExpiration.toMillis())))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException exception) {
            log.error("Invalid signature: {}", exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.error("Invalid token: {}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            log.error("Token is expired: {}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("Token is unsupported: {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("Claims string is empty: {}", exception.getMessage());
        }
        return false;
    }

}
