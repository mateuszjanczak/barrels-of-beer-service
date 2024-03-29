package com.mateuszjanczak.barrelsbeer.domain.repository;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BarrelTapLogRepository extends MongoRepository<BarrelTapLog, String> {
    List<BarrelTapLog> findLogsByDateBetween(Date date, Date date2);

    List<BarrelTapLog> findBarrelTapLogsByDateBetweenOrderByIdDesc(Date date, Date date2);

    List<BarrelTapLog> findBarrelTapLogsByOrderByIdDesc();

    List<BarrelTapLog> findBarrelTapLogByBarrelTapIdOrderByIdDesc(int barrelTapId);

    List<BarrelTapLog> findBarrelTapLogByDateAfter(Date date);

    void deleteAllByBarrelTapId(int barrelTapId);
}
