package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.common.BarrelTapHitException;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelSet;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapAdd;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapHit;
import com.mateuszjanczak.barrelsbeer.domain.dto.ErrorResponse;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.service.BarrelTapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class BarrelTapController {

    private final static String LIST_BARRELS_TAPS = "/barrelTaps";
    private final static String ADD_BARREL_TAP = "/barrelTaps/add";
    private final static String GET_BARREL_TAP = "/barrelTaps/{id}";
    private final static String SET_BARREL_TAP = "/barrelTaps/{id}/set";
    private final static String HIT_BARREL_TAP = "/barrelTaps/{id}/hit/currentLevel/{currentLevel}/temperature/{temperature}";

    private final BarrelTapService barrelTapService;

    public BarrelTapController(BarrelTapService barrelTapService) {
        this.barrelTapService = barrelTapService;
    }

    @GetMapping(GET_BARREL_TAP)
    ResponseEntity<BarrelTap> getBarrelTapById(@PathVariable int id) {
        return new ResponseEntity<>(barrelTapService.getBarrelTapById(id), OK);
    }

    @GetMapping(LIST_BARRELS_TAPS)
    ResponseEntity<List<BarrelTap>> getBarrelTapList() {
        return new ResponseEntity<>(barrelTapService.getBarrelTapList(), OK);
    }

    @PostMapping(ADD_BARREL_TAP)
    ResponseEntity<?> addBeerTap(@Valid @RequestBody BarrelTapAdd barrelTapAdd) {
        barrelTapService.addBeerTap(barrelTapAdd);
        return new ResponseEntity<>(OK);
    }

    @PostMapping(SET_BARREL_TAP)
    ResponseEntity<?> setBarrelOnBeerTap(@PathVariable int id, @Valid @RequestBody BarrelSet barrelSet) {
        barrelTapService.setBarrelOnBeerTap(id, barrelSet);
        return new ResponseEntity<>(OK);
    }

    @GetMapping(HIT_BARREL_TAP)
    ResponseEntity<BarrelTapHit> hitBarrelTap(@PathVariable int id, @PathVariable long currentLevel, @PathVariable float temperature) {
        BarrelTapHit barrelTapHit = barrelTapService.hitBarrelTap(id, currentLevel, temperature);
        return new ResponseEntity<>(barrelTapHit, OK);
    }

    @ExceptionHandler(BarrelTapHitException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleHitBarrelTapException(BarrelTapHitException e) {
        return new ErrorResponse(BAD_REQUEST, e.getMessage());
    }
}
