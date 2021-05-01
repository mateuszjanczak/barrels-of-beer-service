package com.mateuszjanczak.barrelsbeer.domain.repository;

import com.mateuszjanczak.barrelsbeer.domain.entity.BeerLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BeerLogRepository extends MongoRepository<BeerLog, String> {
    Optional<BeerLog> findFirstByOrderByIdDesc();
    Page<BeerLog> findBeerLogsByOrderByEndDate(Pageable pageable);
}
