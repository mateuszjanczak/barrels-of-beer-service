package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTemperatureLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final BarrelTapRepository barrelTapRepository;
    private final BarrelTapLogRepository barrelTapLogRepository;
    private final BarrelTemperatureLogRepository barrelTemperatureLogRepository;

    public AdminService(BarrelTapRepository barrelTapRepository, BarrelTapLogRepository barrelTapLogRepository, BarrelTemperatureLogRepository barrelTemperatureLogRepository) {
        this.barrelTapRepository = barrelTapRepository;
        this.barrelTapLogRepository = barrelTapLogRepository;
        this.barrelTemperatureLogRepository = barrelTemperatureLogRepository;
    }

    public void resetDB() {
        barrelTapRepository.deleteAll();
        barrelTapLogRepository.deleteAll();
        barrelTemperatureLogRepository.deleteAll();
    }
}
