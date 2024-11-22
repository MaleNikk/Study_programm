package com.fluxing.application.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationService<XXX> {
    Flux<XXX> findAll();
    Mono<XXX> findById(String id);
    Mono<XXX> findByName(String name);
    Mono<XXX> save(XXX saved);
    Mono<XXX> update(String id, XXX updated);
    Mono<Boolean> deleteById(String id);
}
