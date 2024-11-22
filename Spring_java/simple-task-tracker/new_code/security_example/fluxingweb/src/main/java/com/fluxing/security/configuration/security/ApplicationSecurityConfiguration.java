package com.fluxing.security.configuration.security;

import com.fluxing.security.configuration.application.AuthenticationEntryPointJwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class ApplicationSecurityConfiguration {

    private final Logger log = LoggerFactory.getLogger(ApplicationSecurityConfiguration.class);

    @Autowired
    private UserDetailsServiceJwt userDetailsServiceJwt;

    @Autowired
    private AuthenticationEntryPointJwt entryPointJwt;

    @Autowired
    private TokenFilterJwt filterJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsServiceJwt);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/webflux/test/task/**").permitAll()
                        .requestMatchers("/webflux/test/user/**").permitAll()
                        .requestMatchers("/webflux/test/auth/**").permitAll()
                        .requestMatchers("/webflux/test/app/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(config -> config.authenticationEntryPoint(entryPointJwt))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSessionManager -> httpSessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(filterJwt, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
