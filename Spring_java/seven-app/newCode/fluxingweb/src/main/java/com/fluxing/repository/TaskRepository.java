package com.fluxing.repository;

import com.fluxing.entity.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Component
@Repository
public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
    Mono<Task> findByName(String name);
}
