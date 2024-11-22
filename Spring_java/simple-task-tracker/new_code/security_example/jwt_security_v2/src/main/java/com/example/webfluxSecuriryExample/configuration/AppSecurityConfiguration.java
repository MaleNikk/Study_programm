package com.example.webfluxSecuriryExample.configuration;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;
import com.example.webfluxSecuriryExample.security.AppSecurityConverter;
import com.example.webfluxSecuriryExample.security.AppSecurityHandler;
import com.example.webfluxSecuriryExample.security.AppSecurityManager;

@Slf4j
@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class AppSecurityConfiguration {

    @Value("${jjwt.secret}")
    private String secret;

    private final String [] publicRouters = {"/webflux/test/security/register", "/webflux/test/security/login"};

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity httpSecurity, AppSecurityManager securityManager) {
        return httpSecurity
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .pathMatchers(publicRouters)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((que, ex) -> {
                    log.error("Web filter checked error: {}", ex.getMessage());
                    return Mono.fromRunnable(() -> que.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                })
                .accessDeniedHandler((que, ex) -> {
                    log.error("Web filter checked bad time for init: {}", ex.getMessage());
                    return Mono.fromRunnable(() -> que.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                })
                .and()
                .addFilterAt(prefixAuthFilter(securityManager), SecurityWebFiltersOrder.AUTHORIZATION)
                .build();
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationWebFilter prefixAuthFilter(AppSecurityManager securityManager) {
        AuthenticationWebFilter webFilter = new AuthenticationWebFilter(securityManager);

        webFilter.setServerAuthenticationConverter(new AppSecurityConverter(new AppSecurityHandler(secret)));
        webFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return webFilter;
    }
}
