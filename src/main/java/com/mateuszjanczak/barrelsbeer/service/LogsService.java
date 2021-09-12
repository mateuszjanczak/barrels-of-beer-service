package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTemperatureLog;
import com.mateuszjanczak.barrelsbeer.domain.entity.BeerLog;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTemperatureLogRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BeerLogRepository;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.mateuszjanczak.barrelsbeer.domain.enums.LogType.BARREL_TAP_READ;

@Service
public class LogsService {

    private final BarrelTapLogRepository barrelTapLogRepository;
    private final BarrelTemperatureLogRepository barrelTemperatureLogRepository;
    private final BeerLogRepository beerLogRepository;
    private final DocumentService documentService;

    public LogsService(BarrelTapLogRepository barrelTapLogRepository, BarrelTemperatureLogRepository barrelTemperatureLogRepository, BeerLogRepository beerLogRepository, DocumentService documentService) {
        this.barrelTapLogRepository = barrelTapLogRepository;
        this.barrelTemperatureLogRepository = barrelTemperatureLogRepository;
        this.beerLogRepository = beerLogRepository;
        this.documentService = documentService;
    }

    public void saveBarrelTapLog(BarrelTap barrelTap, LogType logType) {
        BarrelTapLog barrelTapLog = new BarrelTapLog();
        barrelTapLog.setBarrelTapId(barrelTap.getBarrelTapId());
        barrelTapLog.setBarrelName(barrelTap.getBarrelName());
        barrelTapLog.setBarrelContent(barrelTap.getBarrelContent());
        barrelTapLog.setCurrentLevel(barrelTap.getCurrentLevel());
        barrelTapLog.setTotalUsage(barrelTap.getCapacity() - barrelTap.getCurrentLevel());

        if(logType == BARREL_TAP_READ) {
            barrelTapLog.setSingleUsage(barrelTap.getCapacity() - barrelTap.getCurrentLevel() - getLastTotalUsage(barrelTap.getBarrelTapId()));
        } else {
            barrelTapLog.setSingleUsage(0);
        }

        barrelTapLog.setDate(new Date());
        barrelTapLog.setLogType(logType);
        barrelTapLogRepository.save(barrelTapLog);
    }

    private long getLastTotalUsage(int barrelTapId) {
        return barrelTapLogRepository.findBarrelTapLogByBarrelTapIdOrderByIdDesc(barrelTapId).get(0).getTotalUsage();
    }

    public void saveBarrelTemperatureLog(BarrelTap barrelTap) {
        BarrelTemperatureLog barrelTemperatureLog = new BarrelTemperatureLog();
        barrelTemperatureLog.setBarrelTapId(barrelTap.getBarrelTapId());
        barrelTemperatureLog.setBarrelName(barrelTap.getBarrelName());
        barrelTemperatureLog.setBarrelContent(barrelTap.getBarrelContent());
        barrelTemperatureLog.setTemperature(barrelTap.getTemperature());
        barrelTemperatureLog.setDate(new Date());
        barrelTemperatureLogRepository.save(barrelTemperatureLog);
    }

    public Page<BarrelTapLog> getBarrelTapLogsList(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return barrelTapLogRepository.findAll(pageable);
    }

    public Page<BarrelTemperatureLog> getBarrelTemperatureLogsList(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return barrelTemperatureLogRepository.findAll(pageable);
    }

    public ByteArrayInputStream getBarrelTapLogsListCsv() {
        List<BarrelTapLog> barrelTapLogs = barrelTapLogRepository.findAll();
        return documentService.load(barrelTapLogs);
    }

    public Page<BeerLog> getBeerStatisticsList(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return beerLogRepository.findBeerLogsByOrderByEndDate(pageable);
    }

    @SneakyThrows
    public void generateBeerStatistics() {
        Optional<BeerLog> lastBeerLog = beerLogRepository.findFirstByOrderByIdDesc();

        List<BarrelTapLog> list;

        if(lastBeerLog.isPresent()) {
            Date lastDate = lastBeerLog.get().getEndDate();
            list = barrelTapLogRepository.findBarrelTapLogByDateAfter(lastDate).stream().filter(barrelTapLog -> barrelTapLog.getLogType().equals(BARREL_TAP_READ)).collect(Collectors.toList());
        } else {
            list = barrelTapLogRepository.findAll().stream().filter(barrelTapLog -> barrelTapLog.getLogType().equals(BARREL_TAP_READ)).collect(Collectors.toList());
        }

        Map<String, List<BarrelTapLog>> groupedByBarrelContent = list.stream().collect(Collectors.groupingBy(BarrelTapLog::getBarrelContent));

        for (Map.Entry<String, List<BarrelTapLog>> entry: groupedByBarrelContent.entrySet()) {
            List<BarrelTapLog> barrelTapLogs = entry.getValue();

            Date lastDate = barrelTapLogs.get(0).getDate();
            long amount = 0;

            List<BarrelTapLog> tempList = new ArrayList<>();
            for (int i = 0, barrelTapLogsSize = barrelTapLogs.size(); i < barrelTapLogsSize; i++) {
                BarrelTapLog barrelTapLog = barrelTapLogs.get(i);
                if (barrelTapLog.getLogType().equals(BARREL_TAP_READ)) {
                    long diff = getDateDiff(lastDate, barrelTapLog.getDate(), TimeUnit.SECONDS);

                    if (diff < 5 && i != barrelTapLogsSize - 1) {
                        amount += barrelTapLog.getSingleUsage();
                        tempList.add(barrelTapLog);
                    } else {
                        if(i == barrelTapLogsSize - 1) {
                            amount += barrelTapLog.getSingleUsage();
                            tempList.add(barrelTapLog);
                        }
                        BeerLog beerLog = new BeerLog();
                        beerLog.setBarrelContent(barrelTapLog.getBarrelContent());
                        beerLog.setStartDate(tempList.get(0).getDate());
                        beerLog.setEndDate(tempList.get(tempList.size() - 1).getDate());
                        beerLog.setAmount(amount);
                        beerLog.setBarrelTapLogs(tempList.stream().map(BarrelTapLog::getId).collect(Collectors.toList()));
                        beerLogRepository.save(beerLog);
                        amount = 0;
                        tempList = new ArrayList<>();
                    }
                } else {
                    amount = 0;
                }
                lastDate = barrelTapLog.getDate();
            }
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
