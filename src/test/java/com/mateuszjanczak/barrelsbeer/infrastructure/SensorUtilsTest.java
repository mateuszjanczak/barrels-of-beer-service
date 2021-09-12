package com.mateuszjanczak.barrelsbeer.infrastructure;

import com.mateuszjanczak.barrelsbeer.domain.model.TelemetryData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SensorUtilsTest {

    @Autowired
    SensorUtils sensorUtils;

    @Test
    void getTelemetryData() {
        assertEquals(new TelemetryData(21170, 13.9f), sensorUtils.getTelemetryData("41A95C290000022C"));
        assertEquals(new TelemetryData(214280, 12.7f), sensorUtils.getTelemetryData("435647AE000001FC"));
        assertEquals(new TelemetryData(950, 15.8f), sensorUtils.getTelemetryData("3F73333300000278"));
        assertEquals(new TelemetryData(1560, 14.0f), sensorUtils.getTelemetryData("3FC7AE1400000230"));
    }
}