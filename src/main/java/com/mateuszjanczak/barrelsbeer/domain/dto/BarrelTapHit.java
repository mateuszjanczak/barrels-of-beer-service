package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;

@Data
public class BarrelTapHit {
    int barrelTapId;
    long newCapacity;
    float newTemperature;
}
