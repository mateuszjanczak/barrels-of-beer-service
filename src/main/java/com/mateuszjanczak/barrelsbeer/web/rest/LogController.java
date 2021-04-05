package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.entity.Log;
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

    private final static String ALL_LOGS = "/logs";

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping(ALL_LOGS)
    public ResponseEntity<List<Log>> getLogsList(){
        return new ResponseEntity<>(logService.getLogsList(), HttpStatus.OK);
    }


}
