package com.mateuszjanczak.barrelsbeer.domain.repository;

import com.mateuszjanczak.barrelsbeer.domain.entity.Barrel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarrelRepository extends MongoRepository<Barrel, Integer> {

}
