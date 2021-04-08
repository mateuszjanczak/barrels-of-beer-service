package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final BarrelTapRepository barrelTapRepository;
    private final BarrelTapLogRepository barrelTapLogRepository;

    public AdminService(BarrelTapRepository barrelTapRepository, BarrelTapLogRepository barrelTapLogRepository) {
        this.barrelTapRepository = barrelTapRepository;
        this.barrelTapLogRepository = barrelTapLogRepository;
    }

    public void resetDB() {
        barrelTapRepository.deleteAll();
        barrelTapLogRepository.deleteAll();
    }
}
