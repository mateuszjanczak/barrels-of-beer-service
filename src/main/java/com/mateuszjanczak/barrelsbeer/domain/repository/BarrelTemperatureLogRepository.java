package com.mateuszjanczak.barrelsbeer.domain.repository;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTemperatureLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarrelTemperatureLogRepository extends MongoRepository<BarrelTemperatureLog, String> {
}
