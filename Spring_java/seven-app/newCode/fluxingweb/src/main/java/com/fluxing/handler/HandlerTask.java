package com.fluxing.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface HandlerTask extends ApplicationHandler {
    Mono<ServerResponse> errorRequest(ServerRequest request);
}
