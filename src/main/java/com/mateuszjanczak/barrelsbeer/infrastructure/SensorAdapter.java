package com.mateuszjanczak.barrelsbeer.infrastructure;

import com.mateuszjanczak.barrelsbeer.domain.model.TelemetryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SensorAdapter {

    private final SensorClient sensorClient;
    private final SensorUtils sensorUtils;
    Logger log = LoggerFactory.getLogger(SensorAdapter.class);

    public SensorAdapter(SensorClient sensorClient, SensorUtils sensorUtils) {
        this.sensorClient = sensorClient;
        this.sensorUtils = sensorUtils;
    }

    public Optional<TelemetryData> getTelemetryData(int barrelTapId) {
        Optional<SensorData> optionalSensorData = sensorClient.getSensorData(barrelTapId);

        if (optionalSensorData.isPresent()) {
            SensorData sensorData = optionalSensorData.get();
            String rawData = sensorData.getData().getValue();
            log.info("Data from sensor [ TAP ID: " + barrelTapId + ", RAW DATA: " + rawData + " ]");
            return Optional.ofNullable(sensorUtils.getTelemetryData(rawData));
        }
        return Optional.empty();
    }
}
