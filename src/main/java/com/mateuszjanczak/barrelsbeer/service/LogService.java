package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.domain.entity.Log;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog(BarrelTap barrelTap, LogType logType) {
        Log log = new Log();
        log.setBarrelTapId(barrelTap.getBarrelTapId());
        log.setBarrelName(barrelTap.getBarrelName());
        log.setBarrelContent(barrelTap.getBarrelContent());
        log.setLogType(logType);
        log.setCapacity(barrelTap.getCapacity());
        log.setDate(new Date());
        logRepository.save(log);
    }

    public List<Log> getLogsList() {
        return logRepository.findAll();
    }
}
