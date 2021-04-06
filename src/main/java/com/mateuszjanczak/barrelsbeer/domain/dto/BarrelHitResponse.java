package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;

@Data
public class BarrelHitResponse {
    int barrelId;
    int oldValue;
    int newValue;
}
