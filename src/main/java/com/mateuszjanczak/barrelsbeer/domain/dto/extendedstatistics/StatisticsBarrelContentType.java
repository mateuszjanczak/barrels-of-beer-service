package com.mateuszjanczak.barrelsbeer.domain.dto.extendedstatistics;

import lombok.Data;

import java.util.List;

@Data
public class StatisticsBarrelContentType {
    String name;
    List<StatisticsDates> dates;
}
