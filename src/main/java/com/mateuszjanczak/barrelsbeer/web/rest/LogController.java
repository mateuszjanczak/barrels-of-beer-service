package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTemperatureLog;
import com.mateuszjanczak.barrelsbeer.service.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class LogController {

    private final static String LOGS_BARREL_TAPS = "/logs/barrelTaps";
    private final static String LOGS_BARREL_TEMPERATURE = "/logs/barrelTemperature";

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping(LOGS_BARREL_TAPS)
    public ResponseEntity<List<BarrelTapLog>> getBarrelTapLogsList() {
        return new ResponseEntity<>(logService.getBarrelTapLogsList(), HttpStatus.OK);
    }

    @GetMapping(LOGS_BARREL_TEMPERATURE)
    public ResponseEntity<List<BarrelTemperatureLog>> getBarrelTemperatureLogsList() {
        return new ResponseEntity<>(logService.getBarrelTemperatureLogsList(), HttpStatus.OK);
    }
}
