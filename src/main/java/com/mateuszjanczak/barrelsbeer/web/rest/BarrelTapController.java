package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelSetRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapHitResponse;
import com.mateuszjanczak.barrelsbeer.domain.dto.ErrorResponse;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.common.HitException;
import com.mateuszjanczak.barrelsbeer.service.BarrelTapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    private final static String HEX_BARREL_TAP = "/barrelTaps/{id}/hex/{hex}";

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
    ResponseEntity<?> addBeerTap(@Valid @RequestBody BarrelTapAddRequest barrelTapAddRequest) {
        barrelTapService.addBeerTap(barrelTapAddRequest);
        return new ResponseEntity<>(OK);
    }

    @PostMapping(SET_BARREL_TAP)
    ResponseEntity<?> setBarrelOnBeerTap(@PathVariable int id, @Valid @RequestBody BarrelSetRequest barrelSetRequest) {
        barrelTapService.setBarrelOnBeerTap(id, barrelSetRequest);
        return new ResponseEntity<>(OK);
    }

    @GetMapping(HIT_BARREL_TAP)
    ResponseEntity<BarrelTapHitResponse> hitBarrelTap(@PathVariable int id, @PathVariable long currentLevel, @PathVariable float temperature) {
        BarrelTapHitResponse barrelTapHitResponse = barrelTapService.hitBarrelTap(id, currentLevel, temperature);
        return new ResponseEntity<>(barrelTapHitResponse, OK);
    }

//    @GetMapping(HEX_BARREL_TAP)
//    ResponseEntity<BarrelTapHitResponse> hitBarrelTap(@PathVariable int id, @PathVariable String hex) {
//        Optional<BarrelTapHitResponse> optionalBarrelHitResponse = barrelTapService.hitBarrelTap(id, hex);
//        return optionalBarrelHitResponse.map(barrelTapHitResponse -> new ResponseEntity<>(barrelTapHitResponse, HttpStatus.OK)).orElseThrow(HitException::new);
//    }

    @ExceptionHandler(HitException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleHitBarrelTapException(HitException e) {
        return new ErrorResponse(BAD_REQUEST, "Kranik nie istnieje lub pojemność beczki jest równa 0");
    }
}
