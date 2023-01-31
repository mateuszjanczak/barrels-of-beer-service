package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.common.BarrelTapHitException;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelSet;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapAdd;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapHit;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.domain.mapper.BarrelTapMapper;
import com.mateuszjanczak.barrelsbeer.domain.model.TelemetryData;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mateuszjanczak.barrelsbeer.domain.enums.LogType.*;

@Service
public class BarrelTapService {

    private final BarrelTapRepository barrelTapRepository;
    private final LogsService logsService;
    private final BarrelTapMapper barrelTapMapper;

    public BarrelTapService(BarrelTapRepository barrelTapRepository, LogsService logsService, BarrelTapMapper barrelTapMapper) {
        this.barrelTapRepository = barrelTapRepository;
        this.logsService = logsService;
        this.barrelTapMapper = barrelTapMapper;
    }

    public void addBeerTap(BarrelTapAdd barrelTapAdd) {
        BarrelTap barrelTap = barrelTapMapper.dtoToEntity(barrelTapAdd);
        barrelTapRepository.save(barrelTap);
        logsService.saveBarrelTapLog(barrelTap, BARREL_TAP_NEW);
    }

    public List<BarrelTap> getBarrelTapList() {
        return barrelTapRepository.findAll();
    }

    public void setBarrelOnBeerTap(int id, BarrelSet barrelSet) {
        Optional<BarrelTap> optionalBarrel = barrelTapRepository.findById(id);

        if (optionalBarrel.isPresent()) {
            BarrelTap barrelTap = optionalBarrel.get();
            if (barrelSet.getCapacity() >= 0) {
                barrelTap.setBarrelContent(barrelSet.getBarrelContent().name().replace("_", " "));
                barrelTap.setBarrelName(barrelSet.getBarrelName());
                barrelTap.setCurrentLevel(barrelSet.getCapacity() - barrelTap.getCapacity() + barrelTap.getCurrentLevel());
                barrelTap.setCapacity(barrelSet.getCapacity());
                barrelTapRepository.save(barrelTap);
                logsService.saveBarrelTapLog(barrelTap, BARREL_TAP_SET);
            }
        }
    }

    public void hitBarrelTap(int id, TelemetryData telemetryData) {
        hitBarrelTap(id, telemetryData.getCurrentLevel(), telemetryData.getTemperature());
    }

    public BarrelTap getBarrelTapById(int id) {
        Optional<BarrelTap> optionalBarrelTap = barrelTapRepository.findById(id);
        return optionalBarrelTap.orElse(null);
    }

    public BarrelTapHit hitBarrelTap(int id, long currentLevel, float temperature) {
        Optional<BarrelTap> optionalBarrel = barrelTapRepository.findById(id);

        if (optionalBarrel.isPresent()) {
            BarrelTap barrelTap = optionalBarrel.get();

            long capacity = barrelTap.getCapacity() - currentLevel;

            if (capacity != barrelTap.getCurrentLevel() && barrelTap.getCurrentLevel() > 0) {
                barrelTap.setCurrentLevel(capacity);
                barrelTapRepository.save(barrelTap);
                logsService.saveBarrelTapLog(barrelTap, BARREL_TAP_READ);
            }

            if (temperature != barrelTap.getTemperature()) {
                barrelTap.setTemperature(temperature);
                barrelTapRepository.save(barrelTap);
                //logsService.saveBarrelTemperatureLog(barrelTap);
            }

            return barrelTapMapper.barrelToHitResponse(barrelTap);
        } else {
            throw new BarrelTapHitException();
        }
    }
}
