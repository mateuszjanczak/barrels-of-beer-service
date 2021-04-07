package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.LogRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final BarrelTapRepository barrelTapRepository;
    private final LogRepository logRepository;

    public AdminService(BarrelTapRepository barrelTapRepository, LogRepository logRepository) {
        this.barrelTapRepository = barrelTapRepository;
        this.logRepository = logRepository;
    }

    public void resetDB() {
        barrelTapRepository.deleteAll();
        logRepository.deleteAll();
    }
}
