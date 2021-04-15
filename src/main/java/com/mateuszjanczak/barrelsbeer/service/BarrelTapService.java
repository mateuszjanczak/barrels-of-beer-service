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
            if (barrelSetRequest.getTotalCapacity() >= 0) {
                barrelTap.setBarrelContent(barrelSetRequest.getBarrelContent().name().replace("_", " "));
                barrelTap.setBarrelName(barrelSetRequest.getBarrelName());
                barrelTap.setCapacity(barrelSetRequest.getTotalCapacity());
                barrelTap.setTotalCapacity(barrelSetRequest.getTotalCapacity());
                barrelTapRepository.save(barrelTap);
                logService.saveBarrelTapLog(barrelTap, LogType.BARREL_TAP_SET);
            }
        }
    }

    public Optional<BarrelTapHitResponse> hitBarrelTap(int id, String value) {
        Optional<BarrelTap> optionalBarrel = barrelTapRepository.findById(id);

        TelemetryData telemetryData = telemetryParser.parseRawData(value);

        if (optionalBarrel.isPresent()) {
            BarrelTap barrelTap = optionalBarrel.get();

            long capacity = (long) (barrelTap.getTotalCapacity() - telemetryData.getFlowCounter());

            if (capacity != barrelTap.getCapacity() && barrelTap.getCapacity() > 0) {
                barrelTap.setCapacity(capacity);
                barrelTapRepository.save(barrelTap);
                logService.saveBarrelTapLog(barrelTap, LogType.BARREL_TAP_READ);
            }

            if (telemetryData.getBarrelTemperature() != barrelTap.getTemperature()) {
                barrelTap.setTemperature(telemetryData.getBarrelTemperature());
                barrelTapRepository.save(barrelTap);
                logService.saveBarrelTemperatureLog(barrelTap);
            }

            return Optional.ofNullable(barrelTapMapper.barrelToHitResponse(barrelTap));
        }

        return Optional.empty();
    }

    public BarrelTap getBarrelTapById(int id) {
        Optional<BarrelTap> optionalBarrelTap = barrelTapRepository.findById(id);
        return optionalBarrelTap.orElse(null);
    }
}
