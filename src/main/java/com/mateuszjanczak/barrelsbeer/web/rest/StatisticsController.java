package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.dto.DailyStatistics;
import com.mateuszjanczak.barrelsbeer.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class StatisticsController {

    private final static String DAILY_STATISTICS = "/statistics/daily";

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(DAILY_STATISTICS)
    public ResponseEntity<List<DailyStatistics>> getDailyStatistics() {
        return new ResponseEntity<>(statisticsService.getDailyStatistics(), HttpStatus.OK);
    }
}
