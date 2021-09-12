package com.mateuszjanczak.barrelsbeer.domain.dto.statistics;

import lombok.Data;

import java.util.List;

@Data
public class ContentTypeStatistics {
    String name;
    List<StatisticsData> items;
}
