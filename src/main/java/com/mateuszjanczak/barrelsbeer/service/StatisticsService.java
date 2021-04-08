package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.DailyStatistics;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final BarrelTapLogRepository barrelTapLogRepository;

    public StatisticsService(BarrelTapLogRepository barrelTapLogRepository) {
        this.barrelTapLogRepository = barrelTapLogRepository;
    }

    public List<DailyStatistics> getDailyStatistics() {

        LocalDateTime now = LocalDateTime.now();
        Date min = convertToDateViaInstant(now.with(LocalTime.MIN));
        Date max = convertToDateViaInstant(now.with(LocalTime.MAX));
        List<BarrelTapLog> list = barrelTapLogRepository.findLogsByDateBetween(min, max).stream().filter(barrelTapLog -> barrelTapLog.getLogType().equals(LogType.BARREL_TAP_READ)).collect(Collectors.toList());

        Map<String, Long> collect = list.stream().collect(Collectors.groupingBy(BarrelTapLog::getBarrelContent, Collectors.counting()));

        List<DailyStatistics> dailyStatisticsList = new ArrayList<>();

        for (Map.Entry<String, Long> entry : collect.entrySet()) {
            DailyStatistics dailyStatistics = new DailyStatistics();
            dailyStatistics.setBarrelContent(entry.getKey());
            dailyStatistics.setCount(entry.getValue());
            dailyStatistics.setDate(new Date());
            dailyStatisticsList.add(dailyStatistics);
        }

        dailyStatisticsList.sort(Comparator.comparing(DailyStatistics::getBarrelContent));

        return dailyStatisticsList;
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
