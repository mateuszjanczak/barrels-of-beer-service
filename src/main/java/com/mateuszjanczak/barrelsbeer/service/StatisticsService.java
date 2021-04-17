package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.GlobalStatistics;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
import com.mateuszjanczak.barrelsbeer.domain.dto.extendedstatistics.StatisticsBarrelContentType;
import com.mateuszjanczak.barrelsbeer.domain.dto.extendedstatistics.StatisticsDates;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final BarrelTapLogRepository barrelTapLogRepository;

    public StatisticsService(BarrelTapLogRepository barrelTapLogRepository) {
        this.barrelTapLogRepository = barrelTapLogRepository;
    }

    public List<GlobalStatistics> getAllStatistics() {
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

        List<GlobalStatistics> globalStatisticsList = new ArrayList<>();

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            GlobalStatistics globalStatistics = new GlobalStatistics();
            globalStatistics.setBarrelContent(entry.getKey());
            globalStatistics.setCount(entry.getValue());
            globalStatistics.setDate(new Date());
            globalStatisticsList.add(globalStatistics);
        }

        globalStatisticsList.sort(Comparator.comparing(GlobalStatistics::getCount).reversed());

        return globalStatisticsList;
    }

    @SneakyThrows
    public List<StatisticsBarrelContentType> getExtendedStatistics(String fromDate, String toDate, int interval) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<BarrelTapLog> list = barrelTapLogRepository.findBarrelTapLogsByOrderByIdDesc().stream().filter(barrelTapLog -> barrelTapLog.getLogType().equals(LogType.BARREL_TAP_READ)).collect(Collectors.toList());

        Map<String, List<BarrelTapLog>> groupedByBarrelContent = list.stream().collect(Collectors.groupingBy(BarrelTapLog::getBarrelContent));

        Map<String, Map<String, List<BarrelTapLog>>> groupedByBarrelContentAndDate = new HashMap<>();

        for(Map.Entry<String, List<BarrelTapLog>> entry: groupedByBarrelContent.entrySet()) {
            List<BarrelTapLog> elements = entry.getValue();

            Date min = formatter.parse(fromDate);
            Date max = formatter.parse(toDate);

            Map<String, List<BarrelTapLog>> map = new HashMap<>();
            for (Date from = min, to = DateUtils.addMinutes(from, interval); from.before(max); from = DateUtils.addMinutes(from, interval), to = DateUtils.addMinutes(to, interval)) {
                Date finalFrom = from;
                Date finalTo = to;
                List<BarrelTapLog> betweenList = elements.stream().filter(barrelTapLog -> barrelTapLog.getDate().after(finalFrom) && barrelTapLog.getDate().before(finalTo)).collect(Collectors.toList());
                String key = formatter.format(from) + " - " + formatter.format(to);
                map.put(key, betweenList);
            }
            groupedByBarrelContentAndDate.put(entry.getKey(), map);
        }

        List<StatisticsBarrelContentType> statisticsBarrelContentTypes = new ArrayList<>();

        for(Map.Entry<String, Map<String, List<BarrelTapLog>>> entry: groupedByBarrelContentAndDate.entrySet()) {

            Map<String, List<BarrelTapLog>> barrels = entry.getValue();

            List<StatisticsDates> statisticsDatesList = new ArrayList<>();
            for(Map.Entry<String, List<BarrelTapLog>> dates: barrels.entrySet()) {
                List<BarrelTapLog> barrelTapLogs = dates.getValue();
                long sum = barrelTapLogs.stream().mapToLong(BarrelTapLog::getSingleUsage).sum();
                StatisticsDates statisticsDates = new StatisticsDates();
                statisticsDates.setDate(dates.getKey());
                statisticsDates.setCount(sum);
                statisticsDatesList.add(statisticsDates);
            }

            statisticsDatesList.sort(Comparator.comparing(StatisticsDates::getDate));

            StatisticsBarrelContentType statisticsBarrelContentType = new StatisticsBarrelContentType();
            statisticsBarrelContentType.setName(entry.getKey());
            statisticsBarrelContentType.setDates(statisticsDatesList);
            statisticsBarrelContentTypes.add(statisticsBarrelContentType);
        }

        return statisticsBarrelContentTypes;
    }
}
