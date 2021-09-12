package com.mateuszjanczak.barrelsbeer.domain.model;

import lombok.Data;

@Data
public class TelemetryData {
    long currentLevel;
    float temperature;
}
