package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.dto.GlobalStatistics;
import com.mateuszjanczak.barrelsbeer.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class StatisticsController {

    private final static String ALL_STATISTICS = "/statistics/all";
    private final static String EXTENDED_STATISTICS = "/statistics/from/{from}/to/{to}/interval/{interval}";

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(ALL_STATISTICS)
    public ResponseEntity<List<GlobalStatistics>> getAllStatistics() {
        return new ResponseEntity<>(statisticsService.getAllStatistics(), HttpStatus.OK);
    }

    @GetMapping(EXTENDED_STATISTICS)
    public ResponseEntity<?> getExtendedStatistics(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") String from, @PathVariable int interval, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") String to) {
        return new ResponseEntity<>(statisticsService.getExtendedStatistics(from, to, interval), HttpStatus.OK);
    }
}
