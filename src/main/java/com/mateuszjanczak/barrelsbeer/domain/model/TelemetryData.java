package com.mateuszjanczak.barrelsbeer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryData {
    long currentLevel;
    float temperature;
}
