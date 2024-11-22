package com.fluxing.security.repository;

import com.fluxing.security.dto.UserJwt;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Primary
public interface UserRepositoryJwt extends MongoRepository<UserJwt, Long> {

    Optional<UserJwt> findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);



}
