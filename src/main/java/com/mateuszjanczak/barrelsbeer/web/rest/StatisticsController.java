package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.dto.GlobalStatistics;
import com.mateuszjanczak.barrelsbeer.domain.dto.extendedstatistics.StatisticsBarrelContentType;
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

    private final static String RANKING = "/ranking";
    private final static String STATISTICS = "/statistics/from/{from}/to/{to}/interval/{interval}";

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(RANKING)
    public ResponseEntity<List<GlobalStatistics>> getRanking() {
        return new ResponseEntity<>(statisticsService.getRanking(), HttpStatus.OK);
    }

    @GetMapping(STATISTICS)
    public ResponseEntity<List<StatisticsBarrelContentType>> getStatistics(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") String from, @PathVariable int interval, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") String to) {
        return new ResponseEntity<>(statisticsService.getStatistics(from, to, interval), HttpStatus.OK);
    }
}
