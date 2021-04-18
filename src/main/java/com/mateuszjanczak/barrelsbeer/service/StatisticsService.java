package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.Ranking;
import com.mateuszjanczak.barrelsbeer.domain.dto.statistics.StatisticsBarrelContentType;
import com.mateuszjanczak.barrelsbeer.domain.dto.statistics.StatisticsData;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
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

    public List<Ranking> getRanking() {
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

        List<Ranking> rankingList = new ArrayList<>();

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            Ranking ranking = new Ranking();
            ranking.setBarrelContent(entry.getKey());
            ranking.setCount(entry.getValue());
            ranking.setDate(new Date());
            rankingList.add(ranking);
        }

        rankingList.sort(Comparator.comparing(Ranking::getCount).reversed());

        return rankingList;
    }

    @SneakyThrows
    public List<StatisticsBarrelContentType> getStatistics(String fromDate, String toDate, int interval) {
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

            List<StatisticsData> statisticsItems = new ArrayList<>();
            for(Map.Entry<String, List<BarrelTapLog>> dates: barrels.entrySet()) {
                List<BarrelTapLog> barrelTapLogs = dates.getValue();
                long sum = barrelTapLogs.stream().mapToLong(BarrelTapLog::getSingleUsage).sum();
                StatisticsData statisticsData = new StatisticsData();
                statisticsData.setDate(dates.getKey());
                statisticsData.setCount(sum);
                statisticsItems.add(statisticsData);
            }

            statisticsItems.sort(Comparator.comparing(StatisticsData::getDate));

            StatisticsBarrelContentType statisticsBarrelContentType = new StatisticsBarrelContentType();
            statisticsBarrelContentType.setName(entry.getKey());
            statisticsBarrelContentType.setItems(statisticsItems);
            statisticsBarrelContentTypes.add(statisticsBarrelContentType);
        }

        return statisticsBarrelContentTypes;
    }
}
