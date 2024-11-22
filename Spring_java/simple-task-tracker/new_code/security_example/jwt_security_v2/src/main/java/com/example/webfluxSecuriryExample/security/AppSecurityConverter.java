package com.example.webfluxSecuriryExample.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class AppSecurityConverter  implements ServerAuthenticationConverter {

    private final AppSecurityHandler handler;

    private static final String SECURITY_PREFIX = "Block ";

    private static final Function<String, Mono<String>> getSecurityPrefix =
            authValue -> Mono.justOrEmpty(authValue.substring(SECURITY_PREFIX.length()));

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return extractHeader(exchange)
                .flatMap(getSecurityPrefix)
                .flatMap(handler::check)
                .flatMap(AuthenticationAppUser::create);
    }

    private Mono<String> extractHeader(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
