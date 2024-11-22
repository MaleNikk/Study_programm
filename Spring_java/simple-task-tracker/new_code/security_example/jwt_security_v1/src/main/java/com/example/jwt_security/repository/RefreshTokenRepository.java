package com.example.jwt_security.repository;

import com.example.jwt_security.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier
@EnableJdbcRepositories
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(Long userId);

}
