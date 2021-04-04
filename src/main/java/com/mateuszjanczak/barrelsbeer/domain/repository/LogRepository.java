package com.mateuszjanczak.barrelsbeer.domain.repository;

import com.mateuszjanczak.barrelsbeer.domain.entity.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends MongoRepository<Log, String> {
}
