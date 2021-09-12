package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.dto.ContentRanking;
import com.mateuszjanczak.barrelsbeer.domain.dto.statistics.ContentTypeStatistics;
import com.mateuszjanczak.barrelsbeer.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class StatisticsController {

    private final static String RANKING = "/ranking";
    private final static String STATISTICS = "/statistics/from/{from}/to/{to}/interval/{interval}";

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(RANKING)
    public ResponseEntity<List<ContentRanking>> getRanking() {
        return new ResponseEntity<>(statisticsService.getRanking(), OK);
    }

    @GetMapping(STATISTICS)
    public ResponseEntity<List<ContentTypeStatistics>> getStatistics(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") String from, @PathVariable int interval, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") String to) {
        return new ResponseEntity<>(statisticsService.getStatistics(from, to, interval), OK);
    }
}
