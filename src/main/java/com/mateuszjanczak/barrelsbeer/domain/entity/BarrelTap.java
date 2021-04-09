package com.mateuszjanczak.barrelsbeer.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class BarrelTap {
    @Id
    int barrelTapId;

    String barrelName = "brak nazwy";

    String barrelContent = "brak zawartości";

    float temperature;

    long capacity = 0;

    long totalCapacity = 0;
}