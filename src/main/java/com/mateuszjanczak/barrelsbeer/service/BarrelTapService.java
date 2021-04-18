package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelSetRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapHitResponse;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.mapper.BarrelTapMapper;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapRepository;
import com.mateuszjanczak.barrelsbeer.parser.TelemetryData;
import com.mateuszjanczak.barrelsbeer.parser.TelemetryParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarrelTapService {

    private final BarrelTapRepository barrelTapRepository;
    private final LogService logService;
    private final BarrelTapMapper barrelTapMapper;
    private final TelemetryParser telemetryParser;

    public BarrelTapService(BarrelTapRepository barrelTapRepository, LogService logService, BarrelTapMapper barrelTapMapper, TelemetryParser telemetryParser) {
        this.barrelTapRepository = barrelTapRepository;
        this.logService = logService;
        this.barrelTapMapper = barrelTapMapper;
        this.telemetryParser = telemetryParser;
    }

    public void addBeerTap(BarrelTapAddRequest barrelTapAddRequest) {
        BarrelTap barrelTap = barrelTapMapper.dtoToEntity(barrelTapAddRequest);
        barrelTapRepository.save(barrelTap);
        logService.saveBarrelTapLog(barrelTap, LogType.BARREL_TAP_NEW);
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
                barrelTap.setCurrentLevel(barrelSetRequest.getCapacity());
                barrelTap.setCapacity(barrelSetRequest.getCapacity());
                barrelTapRepository.save(barrelTap);
                logService.saveBarrelTapLog(barrelTap, LogType.BARREL_TAP_SET);
            }
        }
    }

    public Optional<BarrelTapHitResponse> hitBarrelTap(int id, String value) {
        TelemetryData telemetryData = telemetryParser.parseRawData(value);
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

            if (currentLevel != barrelTap.getCurrentLevel() && barrelTap.getCurrentLevel() > 0) {
                barrelTap.setCurrentLevel(capacity);
                barrelTapRepository.save(barrelTap);
                logService.saveBarrelTapLog(barrelTap, LogType.BARREL_TAP_READ);
            }

            if (temperature != barrelTap.getTemperature()) {
                barrelTap.setTemperature(temperature);
                barrelTapRepository.save(barrelTap);
                logService.saveBarrelTemperatureLog(barrelTap);
            }

            return Optional.ofNullable(barrelTapMapper.barrelToHitResponse(barrelTap));
        }

        return Optional.empty();
    }
}
