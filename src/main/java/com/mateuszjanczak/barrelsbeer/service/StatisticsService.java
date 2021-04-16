package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.DailyStatistics;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final BarrelTapLogRepository barrelTapLogRepository;

    public StatisticsService(BarrelTapLogRepository barrelTapLogRepository) {
        this.barrelTapLogRepository = barrelTapLogRepository;
    }

    public List<DailyStatistics> getAllStatistics() {

       /* LocalDateTime now = LocalDateTime.now();
        Date min = convertToDateViaInstant(now.with(LocalTime.MIN));
        Date max = convertToDateViaInstant(now.with(LocalTime.MAX));

        List<BarrelTapLog> list = barrelTapLogRepository.findBarrelTapLogsByDateBetweenOrderByIdDesc(min, max).stream().filter(barrelTapLog -> barrelTapLog.getLogType().equals(LogType.BARREL_TAP_READ)).collect(Collectors.toList());
*/
        List<BarrelTapLog> list = barrelTapLogRepository.findBarrelTapLogsByOrderByIdDesc().stream().filter(barrelTapLog -> barrelTapLog.getLogType().equals(LogType.BARREL_TAP_READ)).collect(Collectors.toList());

        Map<String, List<BarrelTapLog>> groupedByBarrelContent = list.stream().collect(Collectors.groupingBy(BarrelTapLog::getBarrelContent));

        Map<String, Long> map = new HashMap<>();

        for (Map.Entry<String, List<BarrelTapLog>> entry : groupedByBarrelContent.entrySet()) {
            long sum = 0;
            List<BarrelTapLog> barrelTapLogs = entry.getValue();
            for (int i = 0, barrelTapLogsSize = barrelTapLogs.size(); i < barrelTapLogsSize; i++) {

                long first = barrelTapLogs.get(i).getTotalUsage();

                if(i == barrelTapLogs.size() - 1) {
                    sum += first;
                    map.put(entry.getKey(), sum);
                    break;
                }

                long second = barrelTapLogs.get(i+1).getTotalUsage();
                long diff = first - second;

                if(diff > 0) {
                    sum += diff;
                } else {
                    sum += first;
                }

                map.put(entry.getKey(), sum);
            }
        }

        List<DailyStatistics> dailyStatisticsList = new ArrayList<>();

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            DailyStatistics dailyStatistics = new DailyStatistics();
            dailyStatistics.setBarrelContent(entry.getKey());
            dailyStatistics.setCount(entry.getValue());
            dailyStatistics.setDate(new Date());
            dailyStatisticsList.add(dailyStatistics);
        }

        dailyStatisticsList.sort(Comparator.comparing(DailyStatistics::getCount).reversed());

        return dailyStatisticsList;
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
