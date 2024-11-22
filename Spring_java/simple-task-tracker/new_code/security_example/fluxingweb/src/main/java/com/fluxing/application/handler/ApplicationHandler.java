package com.fluxing.application.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ApplicationHandler {
    Mono<ServerResponse> getAll(ServerRequest request);
    Mono<ServerResponse> findById(ServerRequest request);
    Mono<ServerResponse> findByName(ServerRequest request);
    Mono<ServerResponse> create(ServerRequest request);
    Mono<ServerResponse> update(ServerRequest request);
    Mono<ServerResponse> deleteById(ServerRequest request);
}
