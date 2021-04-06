package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.entity.Barrel;
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

    public void saveLog(Barrel barrel, LogType logType) {
        Log log = new Log();
        log.setBarrelId(barrel.getId());
        log.setBarrelName(barrel.getBarrelName());
        log.setLogType(logType);
        log.setCapacity(barrel.getCapacity());
        log.setDate(new Date());
        logRepository.save(log);
    }

    public List<Log> getLogsList() {
        return logRepository.findAll();
    }
}
