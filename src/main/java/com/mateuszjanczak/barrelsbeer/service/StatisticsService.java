package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.DailyStatistics;
import com.mateuszjanczak.barrelsbeer.domain.entity.Log;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final LogRepository logRepository;

    public StatisticsService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<DailyStatistics> getDailyStatistics() {

        LocalDateTime now = LocalDateTime.now();
        Date min = convertToDateViaInstant(now.with(LocalTime.MIN));
        Date max = convertToDateViaInstant(now.with(LocalTime.MAX));
        List<Log> list = logRepository.findLogsByDateBetween(min, max).stream().filter(log -> log.getLogType().equals(LogType.BARREL_HIT)).collect(Collectors.toList());

        Map<String, Long> collect = list.stream().collect(Collectors.groupingBy(Log::getBarrelName, Collectors.counting()));

        List<DailyStatistics> dailyStatisticsList = new ArrayList<>();

        for (Map.Entry<String, Long> entry: collect.entrySet()) {
            DailyStatistics dailyStatistics = new DailyStatistics();
            dailyStatistics.setBarrelName(entry.getKey());
            dailyStatistics.setCount(entry.getValue());
            dailyStatistics.setDate(new Date());
            dailyStatistics.setBeerType(list.stream().filter(log -> log.getBarrelName().equals(entry.getKey())).findFirst().get().getBeerType());
            dailyStatisticsList.add(dailyStatistics);
        }

        dailyStatisticsList.sort(Comparator.comparing(DailyStatistics::getBarrelName));

        return dailyStatisticsList;
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
