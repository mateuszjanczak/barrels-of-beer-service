package com.mateuszjanczak.barrelsbeer.infrastructure;

import lombok.Data;

@Data
public class TelemetryData {
    long currentLevel;
    float temperature;
}
