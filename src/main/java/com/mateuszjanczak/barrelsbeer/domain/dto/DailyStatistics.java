package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DailyStatistics {
    Date date;
    String barrelContent;
    Long count;
}
