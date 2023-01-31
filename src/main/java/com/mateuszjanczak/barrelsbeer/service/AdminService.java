package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.domain.enums.TableType;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTemperatureLogRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BeerLogRepository;
import com.mateuszjanczak.barrelsbeer.infrastructure.SensorScheduler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final BarrelTapRepository barrelTapRepository;
    private final BarrelTapLogRepository barrelTapLogRepository;
    private final BarrelTemperatureLogRepository barrelTemperatureLogRepository;
    private final BeerLogRepository beerLogRepository;
    private final SensorScheduler sensorScheduler;

    public AdminService(BarrelTapRepository barrelTapRepository, BarrelTapLogRepository barrelTapLogRepository, BarrelTemperatureLogRepository barrelTemperatureLogRepository, BeerLogRepository beerLogRepository, SensorScheduler sensorScheduler) {
        this.barrelTapRepository = barrelTapRepository;
        this.barrelTapLogRepository = barrelTapLogRepository;
        this.barrelTemperatureLogRepository = barrelTemperatureLogRepository;
        this.beerLogRepository = beerLogRepository;
        this.sensorScheduler = sensorScheduler;
    }

    public void resetDatabase(TableType table) {
        switch (table) {
            case BARREL_TAP:
                barrelTapRepository.deleteAll();
                break;
            case BARREL_TAP_LOG:
                barrelTapLogRepository.deleteAll();
                break;
            case BARREL_TEMPERATURE_LOG:
                barrelTemperatureLogRepository.deleteAll();
                break;
            case BEER_LOG:
                beerLogRepository.deleteAll();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + table);
        }
    }

    public void setTapEnabled(int id, boolean enabled) {

        Optional<BarrelTap> optionalBarrelTap = barrelTapRepository.findById(id);

        if (optionalBarrelTap.isPresent()) {
            BarrelTap barrelTap = optionalBarrelTap.get();
            barrelTap.setEnabled(enabled);
            barrelTapRepository.save(barrelTap);
        }

        sensorScheduler.refreshTaps();
    }

    public void resetTap(int id) {
        barrelTapRepository.deleteById(id);
        barrelTapLogRepository.deleteAllByBarrelTapId(id);
        barrelTemperatureLogRepository.deleteAllByBarrelTapId(id);
    }
}
