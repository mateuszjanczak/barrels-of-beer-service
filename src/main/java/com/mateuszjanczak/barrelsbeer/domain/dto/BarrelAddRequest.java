package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;

@Data
public class BarrelAddRequest {
    String barrelName;
    int totalCapacity;
}
