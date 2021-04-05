package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DailyStatistics {
    Date date;
    String barrelName;
    String beerType;
    Long count;
}
