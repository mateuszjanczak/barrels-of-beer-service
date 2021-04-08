package com.mateuszjanczak.barrelsbeer.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class BarrelTemperatureLog {
    @Id
    String id;

    int barrelTapId;

    String barrelName;

    String barrelContent;

    float temperature;

    Date date;
}
