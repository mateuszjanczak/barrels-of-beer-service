package com.mateuszjanczak.barrelsbeer.domain.repository;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BarrelTapLogRepository extends MongoRepository<BarrelTapLog, String> {
    List<BarrelTapLog> findLogsByDateBetween(Date date, Date date2);
}
