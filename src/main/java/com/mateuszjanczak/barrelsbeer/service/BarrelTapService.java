package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelSetRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapHitResponse;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.mapper.BarrelTapMapper;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapRepository;
import com.mateuszjanczak.barrelsbeer.domain.model.TelemetryData;
import com.mateuszjanczak.barrelsbeer.infrastructure.SensorAdapter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarrelTapService {

    private final BarrelTapRepository barrelTapRepository;
    private final LogsService logsService;
    private final BarrelTapMapper barrelTapMapper;
    private final SensorAdapter sensorAdapter;

    public BarrelTapService(BarrelTapRepository barrelTapRepository, LogsService logsService, BarrelTapMapper barrelTapMapper, SensorAdapter sensorAdapter) {
        this.barrelTapRepository = barrelTapRepository;
        this.logsService = logsService;
        this.barrelTapMapper = barrelTapMapper;
        this.sensorAdapter = sensorAdapter;
    }

    public void addBeerTap(BarrelTapAddRequest barrelTapAddRequest) {
        BarrelTap barrelTap = barrelTapMapper.dtoToEntity(barrelTapAddRequest);
        barrelTapRepository.save(barrelTap);
        logsService.saveBarrelTapLog(barrelTap, LogType.BARREL_TAP_NEW);
    }

    public List<BarrelTap> getBarrelTapList() {
        return barrelTapRepository.findAll();
    }

    public void setBarrelOnBeerTap(int id, BarrelSetRequest barrelSetRequest) {
        Optional<BarrelTap> optionalBarrel = barrelTapRepository.findById(id);

        if (optionalBarrel.isPresent()) {
            BarrelTap barrelTap = optionalBarrel.get();
            if (barrelSetRequest.getCapacity() >= 0) {
                barrelTap.setBarrelContent(barrelSetRequest.getBarrelContent().name().replace("_", " "));
                barrelTap.setBarrelName(barrelSetRequest.getBarrelName());
                barrelTap.setCurrentLevel(barrelSetRequest.getCapacity() - barrelTap.getCapacity() + barrelTap.getCurrentLevel());
                barrelTap.setCapacity(barrelSetRequest.getCapacity());
                barrelTapRepository.save(barrelTap);
                logsService.saveBarrelTapLog(barrelTap, LogType.BARREL_TAP_SET);
            }
        }
    }

    public Optional<BarrelTapHitResponse> hitBarrelTap(int id, TelemetryData telemetryData) {
        return hitBarrelTap(id, telemetryData.getCurrentLevel(), telemetryData.getTemperature());
    }

    public BarrelTap getBarrelTapById(int id) {
        Optional<BarrelTap> optionalBarrelTap = barrelTapRepository.findById(id);
        return optionalBarrelTap.orElse(null);
    }

    public Optional<BarrelTapHitResponse> hitBarrelTap(int id, long currentLevel, float temperature) {
        Optional<BarrelTap> optionalBarrel = barrelTapRepository.findById(id);

        if (optionalBarrel.isPresent()) {
            BarrelTap barrelTap = optionalBarrel.get();

            long capacity = barrelTap.getCapacity() - currentLevel;

            if (capacity != barrelTap.getCurrentLevel() && barrelTap.getCurrentLevel() > 0) {
                barrelTap.setCurrentLevel(capacity);
                barrelTapRepository.save(barrelTap);
                logsService.saveBarrelTapLog(barrelTap, LogType.BARREL_TAP_READ);
            }

            if (temperature != barrelTap.getTemperature()) {
                barrelTap.setTemperature(temperature);
                barrelTapRepository.save(barrelTap);
                logsService.saveBarrelTemperatureLog(barrelTap);
            }

            return Optional.ofNullable(barrelTapMapper.barrelToHitResponse(barrelTap));
        }

        return Optional.empty();
    }
}
