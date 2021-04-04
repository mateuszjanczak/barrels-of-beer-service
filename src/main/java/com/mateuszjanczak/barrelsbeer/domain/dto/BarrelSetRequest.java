package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;

@Data
public class BarrelSetRequest {
    String beerType;
    int capacity;
}
