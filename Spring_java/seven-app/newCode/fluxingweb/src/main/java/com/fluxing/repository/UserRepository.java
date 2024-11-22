package com.fluxing.repository;

import com.fluxing.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Component
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByName(String name);
}
