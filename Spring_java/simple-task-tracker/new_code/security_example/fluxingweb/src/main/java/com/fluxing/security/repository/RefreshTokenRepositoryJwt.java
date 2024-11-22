package com.fluxing.security.repository;

import com.fluxing.security.dto.RefreshTokenJwt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepositoryJwt extends CrudRepository<RefreshTokenJwt, Long> {

    Optional<RefreshTokenJwt> findByToken(String token);

    void deleteByUserId(String userId);

}
