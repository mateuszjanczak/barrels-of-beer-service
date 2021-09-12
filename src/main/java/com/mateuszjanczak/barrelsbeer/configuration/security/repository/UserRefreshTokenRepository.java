package com.mateuszjanczak.barrelsbeer.configuration.security.repository;

import com.mateuszjanczak.barrelsbeer.configuration.security.entity.UserRefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRefreshTokenRepository extends MongoRepository<UserRefreshToken, String> {
    Optional<UserRefreshToken> findByToken(String token);
}
