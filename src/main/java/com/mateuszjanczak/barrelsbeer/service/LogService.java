package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTemperatureLog;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapLogRepository;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTemperatureLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class LogService {
    private final BarrelTapLogRepository barrelTapLogRepository;
    private final BarrelTemperatureLogRepository barrelTemperatureLogRepository;
    private Date temperatureDate;

    public LogService(BarrelTapLogRepository barrelTapLogRepository, BarrelTemperatureLogRepository barrelTemperatureLogRepository) {
        this.barrelTapLogRepository = barrelTapLogRepository;
        this.barrelTemperatureLogRepository = barrelTemperatureLogRepository;
        temperatureDate = new Date();
    }

    public void saveBarrelTapLog(BarrelTap barrelTap, LogType logType) {
        BarrelTapLog barrelTapLog = new BarrelTapLog();
        barrelTapLog.setBarrelTapId(barrelTap.getBarrelTapId());
        barrelTapLog.setBarrelName(barrelTap.getBarrelName());
        barrelTapLog.setBarrelContent(barrelTap.getBarrelContent());
        barrelTapLog.setCurrentLevel(barrelTap.getCurrentLevel());
        barrelTapLog.setTotalUsage(barrelTap.getCapacity() - barrelTap.getCurrentLevel());

        if(logType == LogType.BARREL_TAP_READ) {
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
        Date now = new Date();
        if(now.after(temperatureDate)) {
            BarrelTemperatureLog barrelTemperatureLog = new BarrelTemperatureLog();
            barrelTemperatureLog.setBarrelTapId(barrelTap.getBarrelTapId());
            barrelTemperatureLog.setBarrelName(barrelTap.getBarrelName());
            barrelTemperatureLog.setBarrelContent(barrelTap.getBarrelContent());
            barrelTemperatureLog.setTemperature(barrelTap.getTemperature());
            barrelTemperatureLog.setDate(new Date());
            barrelTemperatureLogRepository.save(barrelTemperatureLog);
            temperatureDate = convertToDateViaInstant(LocalDateTime.now().plusMinutes(5));
        }
    }

    public Page<BarrelTapLog> getBarrelTapLogsList(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return barrelTapLogRepository.findAll(pageable);
    }

    public Page<BarrelTemperatureLog> getBarrelTemperatureLogsList(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return barrelTemperatureLogRepository.findAll(pageable);
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
