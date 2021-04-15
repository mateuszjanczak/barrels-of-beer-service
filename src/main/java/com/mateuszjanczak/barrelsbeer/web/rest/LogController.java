package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTemperatureLog;
import com.mateuszjanczak.barrelsbeer.service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class LogController {

    private final static String LOGS_BARREL_TAPS = "/logs/barrelTaps/{page}";
    private final static String LOGS_BARREL_TEMPERATURE = "/logs/barrelTemperature/{page}";

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping(LOGS_BARREL_TAPS)
    public ResponseEntity<Page<BarrelTapLog>> getBarrelTapLogsList(@PathVariable int page) {
        Page<BarrelTapLog> barrelTapLogsList = logService.getBarrelTapLogsList(page);
        return new ResponseEntity<>(barrelTapLogsList, HttpStatus.OK);
    }

    @GetMapping(LOGS_BARREL_TEMPERATURE)
    public ResponseEntity<Page<BarrelTemperatureLog>> getBarrelTemperatureLogsList(@PathVariable int page) {
        Page<BarrelTemperatureLog> barrelTemperatureLogsList = logService.getBarrelTemperatureLogsList(page);
        return new ResponseEntity<>(barrelTemperatureLogsList, HttpStatus.OK);
    }
}
