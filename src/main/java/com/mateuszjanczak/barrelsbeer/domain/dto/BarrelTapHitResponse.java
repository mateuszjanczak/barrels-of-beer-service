package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;

@Data
public class BarrelTapHitResponse {
    int barrelTapId;
    int oldValue;
    int newValue;
}