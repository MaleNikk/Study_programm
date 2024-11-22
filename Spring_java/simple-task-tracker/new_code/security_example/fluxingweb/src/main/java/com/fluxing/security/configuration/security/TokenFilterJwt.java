package com.fluxing.security.configuration.security;

import com.fluxing.security.configuration.application.ApplicationJwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilterJwt extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(TokenFilterJwt.class);

    @Autowired
    private ApplicationJwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceJwt userDetailsServiceJwt;

    @Override
    protected void doFilterInternal
            (
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    )
            throws ServletException, IOException {

        try {
            String jwtToken = getToken(request);

            if (jwtToken != null && jwtUtils.validateToken(jwtToken)) {

                log.info("Test filter chain: jwt token {}", jwtToken);

                String username = jwtUtils.getUsername(jwtToken);

                UserDetails userDetails = userDetailsServiceJwt.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception exception) {

            log.error("Cannot set user authentication: {}", exception.getMessage());
        }

        filterChain.doFilter(request, response);

    }

    private String getToken(HttpServletRequest request) {

        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            log.info("Check token: {}", headerAuth.substring(7));
            return headerAuth.substring(7);
        }
        return null;
    }
}
