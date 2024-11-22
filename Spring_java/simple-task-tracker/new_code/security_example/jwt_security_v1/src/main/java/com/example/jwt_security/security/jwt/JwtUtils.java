package com.example.jwt_security.security.jwt;

import com.example.jwt_security.security.AppUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateJwtToken(AppUserDetails userDetails) {
        return generateTokenFromUsername(userDetails.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public String getUsername(String token) {
        return Jwts.parser()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        }


    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
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
