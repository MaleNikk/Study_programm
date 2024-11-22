package com.example.webfluxSecuriryExample.repository;

import com.example.webfluxSecuriryExample.entity.AppUser;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
@EnableMongoRepositories
public interface UserRepository extends ReactiveCrudRepository<AppUser, String> {

   Mono<AppUser> findByUserName(String userName);


}
