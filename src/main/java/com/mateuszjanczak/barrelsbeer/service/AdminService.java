package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.LogRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final BarrelRepository barrelRepository;
    private final LogRepository logRepository;

    public AdminService(BarrelRepository barrelRepository, LogRepository logRepository) {
        this.barrelRepository = barrelRepository;
        this.logRepository = logRepository;
    }

    public void resetDB() {
        barrelRepository.deleteAll();
        logRepository.deleteAll();
    }
}
