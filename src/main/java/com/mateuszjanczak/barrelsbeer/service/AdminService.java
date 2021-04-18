package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTemperatureLogRepository;
import com.mateuszjanczak.barrelsbeer.driver.SensorScheduler;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final BarrelTapRepository barrelTapRepository;
    private final BarrelTapLogRepository barrelTapLogRepository;
    private final BarrelTemperatureLogRepository barrelTemperatureLogRepository;
    private final SensorScheduler sensorScheduler;

    public AdminService(BarrelTapRepository barrelTapRepository, BarrelTapLogRepository barrelTapLogRepository, BarrelTemperatureLogRepository barrelTemperatureLogRepository, SensorScheduler sensorScheduler) {
        this.barrelTapRepository = barrelTapRepository;
        this.barrelTapLogRepository = barrelTapLogRepository;
        this.barrelTemperatureLogRepository = barrelTemperatureLogRepository;
        this.sensorScheduler = sensorScheduler;
    }

    public void resetDB() {
        barrelTapRepository.deleteAll();
        barrelTapLogRepository.deleteAll();
        barrelTemperatureLogRepository.deleteAll();
    }

    public void enableTaps() {
        sensorScheduler.enableTaps();
    }

    public void disableTaps() {
        sensorScheduler.disableTaps();
    }
}
