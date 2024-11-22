package com.fluxing.application.repository;

import com.fluxing.application.entity.Task;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Qualifier
public interface TaskRepository extends ReactiveMongoRepository<Task, String> {

    Mono<Task> findByName(String name);

}
