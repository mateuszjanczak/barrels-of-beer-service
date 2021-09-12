package com.mateuszjanczak.barrelsbeer.infrastructure;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.domain.model.TelemetryData;
import com.mateuszjanczak.barrelsbeer.service.BarrelTapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SensorScheduler {

    private final BarrelTapService barrelTapService;
    private final SensorAdapter sensorAdapter;

    Logger log = LoggerFactory.getLogger(SensorScheduler.class);

    private List<BarrelTap> barrelTapList;

    public SensorScheduler(BarrelTapService barrelTapService, SensorAdapter sensorAdapter) {
        this.barrelTapService = barrelTapService;
        this.sensorAdapter = sensorAdapter;
        refreshTaps();
    }

    public void refreshTaps() {
        barrelTapList = barrelTapService.getBarrelTapList();
    }

    @Scheduled(fixedRate = 1000)
    public void readData() {
        barrelTapList.stream()
                .filter(BarrelTap::isEnabled)
                .map(BarrelTap::getBarrelTapId)
                .forEach(this::getSensorData);
    }

    private void getSensorData(int barrelTapId) {
        new Thread(() -> {
            Optional<TelemetryData> optionalTelemetryData = sensorAdapter.getTelemetryData(barrelTapId);
            if (optionalTelemetryData.isPresent()) {
                TelemetryData telemetryData = optionalTelemetryData.get();
                barrelTapService.hitBarrelTap(barrelTapId, telemetryData);
            } else {
                log.error("Error with read data from sensor " + barrelTapId);
            }
        }).start();
    }
}
