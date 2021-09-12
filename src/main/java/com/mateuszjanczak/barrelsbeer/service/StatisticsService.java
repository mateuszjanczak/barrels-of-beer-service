package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.ContentRanking;
import com.mateuszjanczak.barrelsbeer.domain.dto.statistics.ContentTypeStatistics;
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

    public List<ContentRanking> getRanking() {
        List<BarrelTapLog> list = barrelTapLogRepository.findBarrelTapLogsByOrderByIdDesc().stream().filter(barrelTapLog -> barrelTapLog.getLogType().equals(LogType.BARREL_TAP_READ)).collect(Collectors.toList());

        Map<String, List<BarrelTapLog>> groupedByBarrelContent = list.stream().collect(Collectors.groupingBy(BarrelTapLog::getBarrelContent));

        List<ContentRanking> contentRankingList = new ArrayList<>();

        for (Map.Entry<String, List<BarrelTapLog>> entry : groupedByBarrelContent.entrySet()) {
            List<BarrelTapLog> barrelTapLogs = entry.getValue();

            long sum = barrelTapLogs.stream().map(BarrelTapLog::getSingleUsage).reduce(0L, Long::sum);

            ContentRanking contentRanking = new ContentRanking();
            contentRanking.setDate(new Date());
            contentRanking.setBarrelContent(barrelTapLogs.get(0).getBarrelContent());
            contentRanking.setCount(sum);

            contentRankingList.add(contentRanking);
        }

        contentRankingList.sort(Comparator.comparing(ContentRanking::getCount).reversed());

        return contentRankingList;
    }

    @SneakyThrows
    public List<ContentTypeStatistics> getStatistics(String fromDate, String toDate, int interval) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<BarrelTapLog> list = barrelTapLogRepository.findBarrelTapLogsByOrderByIdDesc().stream().filter(barrelTapLog -> barrelTapLog.getLogType().equals(LogType.BARREL_TAP_READ)).collect(Collectors.toList());

        Map<String, List<BarrelTapLog>> groupedByBarrelContent = list.stream().collect(Collectors.groupingBy(BarrelTapLog::getBarrelContent));

        Map<String, Map<String, List<BarrelTapLog>>> groupedByBarrelContentAndDate = new HashMap<>();

        for (Map.Entry<String, List<BarrelTapLog>> entry : groupedByBarrelContent.entrySet()) {
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

        List<ContentTypeStatistics> contentTypeStatisticsList = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<BarrelTapLog>>> entry : groupedByBarrelContentAndDate.entrySet()) {

            Map<String, List<BarrelTapLog>> barrels = entry.getValue();

            List<StatisticsData> statisticsItems = new ArrayList<>();
            for (Map.Entry<String, List<BarrelTapLog>> dates : barrels.entrySet()) {
                List<BarrelTapLog> barrelTapLogs = dates.getValue();
                long sum = barrelTapLogs.stream().mapToLong(BarrelTapLog::getSingleUsage).sum();
                StatisticsData statisticsData = new StatisticsData();
                statisticsData.setDate(dates.getKey());
                statisticsData.setCount(sum);
                statisticsItems.add(statisticsData);
            }

            statisticsItems.sort(Comparator.comparing(StatisticsData::getDate));

            ContentTypeStatistics contentTypeStatistics = new ContentTypeStatistics();
            contentTypeStatistics.setName(entry.getKey());
            contentTypeStatistics.setItems(statisticsItems);
            contentTypeStatisticsList.add(contentTypeStatistics);
        }

        return contentTypeStatisticsList;
    }
}
