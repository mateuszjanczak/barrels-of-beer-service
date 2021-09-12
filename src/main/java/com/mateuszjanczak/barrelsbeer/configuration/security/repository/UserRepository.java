package com.mateuszjanczak.barrelsbeer.configuration.security.repository;

import com.mateuszjanczak.barrelsbeer.configuration.security.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
