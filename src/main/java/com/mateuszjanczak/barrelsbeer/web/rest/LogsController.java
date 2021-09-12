package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTemperatureLog;
import com.mateuszjanczak.barrelsbeer.domain.entity.BeerLog;
import com.mateuszjanczak.barrelsbeer.service.LogsService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class LogsController {

    private final static String LOGS_BARREL_TAPS = "/logs/barrelTaps/{page}";
    private final static String LOGS_BARREL_TAPS_CSV = "/logs/barrelTaps/csv";
    private final static String LOGS_BARREL_TEMPERATURE = "/logs/barrelTemperature/{page}";
    private final static String LOGS_BEERS = "/logs/beers/{page}";
    private final static String LOGS_BEERS_UPDATE = "/logs/beers/update";

    private final LogsService logsService;

    public LogsController(LogsService logsService) {
        this.logsService = logsService;
    }

    @GetMapping(LOGS_BARREL_TAPS)
    public ResponseEntity<Page<BarrelTapLog>> getBarrelTapLogsList(@PathVariable int page) {
        Page<BarrelTapLog> barrelTapLogsList = logsService.getBarrelTapLogsList(page);
        return new ResponseEntity<>(barrelTapLogsList, OK);
    }

    @GetMapping(LOGS_BARREL_TAPS_CSV)
    public ResponseEntity<Resource> getBarrelTapLogsListCsv() {
        Resource resource = new InputStreamResource(logsService.getBarrelTapLogsListCsv());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=barrelTapLogs.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    @GetMapping(LOGS_BARREL_TEMPERATURE)
    public ResponseEntity<Page<BarrelTemperatureLog>> getBarrelTemperatureLogsList(@PathVariable int page) {
        Page<BarrelTemperatureLog> barrelTemperatureLogsList = logsService.getBarrelTemperatureLogsList(page);
        return new ResponseEntity<>(barrelTemperatureLogsList, OK);
    }

    @GetMapping(LOGS_BEERS)
    public ResponseEntity<Page<BeerLog>> getBeerStatisticsList(@PathVariable int page) {
        Page<BeerLog> beerLogsList = logsService.getBeerStatisticsList(page);
        return new ResponseEntity<>(beerLogsList, OK);
    }

    @PostMapping(LOGS_BEERS_UPDATE)
    public ResponseEntity<Void> generateBeerStatistics() {
        logsService.generateBeerStatistics();
        return new ResponseEntity<>(OK);
    }
}
