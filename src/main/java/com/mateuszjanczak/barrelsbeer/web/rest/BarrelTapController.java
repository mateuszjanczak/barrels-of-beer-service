package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelSetRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapHitResponse;
import com.mateuszjanczak.barrelsbeer.domain.dto.ErrorResponse;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.exception.HitException;
import com.mateuszjanczak.barrelsbeer.service.BarrelTapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class BarrelTapController {

    private final static String LIST_BARRELS_TAPS = "/barrelTaps";
    private final static String ADD_BARREL_TAP = "/barrelTaps/add";
    private final static String GET_BARREL_TAP = "/barrelTaps/{id}";
    private final static String SET_BARREL_TAP = "/barrelTaps/{id}/set";
    private final static String HIT_BARREL_TAP = "/barrelTaps/{id}/hit/{value}";

    private final BarrelTapService barrelTapService;

    public BarrelTapController(BarrelTapService barrelTapService) {
        this.barrelTapService = barrelTapService;
    }

    @GetMapping(GET_BARREL_TAP)
    ResponseEntity<BarrelTap> getBarrelTapById(@PathVariable int id) {
        return new ResponseEntity<>(barrelTapService.getBarrelTapById(id), HttpStatus.OK);
    }

    @GetMapping(LIST_BARRELS_TAPS)
    ResponseEntity<List<BarrelTap>> getBarrelTapList(){
        return new ResponseEntity<>(barrelTapService.getBarrelTapList(), HttpStatus.OK);
    }

    @PostMapping(ADD_BARREL_TAP)
    ResponseEntity<?> addBeerTap(@Valid @RequestBody BarrelTapAddRequest barrelTapAddRequest) {
        barrelTapService.addBeerTap(barrelTapAddRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(SET_BARREL_TAP)
    ResponseEntity<?> setBarrelOnBeerTap(@PathVariable int id, @Valid @RequestBody BarrelSetRequest barrelSetRequest) {
        barrelTapService.setBarrelOnBeerTap(id, barrelSetRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(HIT_BARREL_TAP)
    ResponseEntity<BarrelTapHitResponse> hitBarrelTap(@PathVariable int id, @PathVariable int value) {
        Optional<BarrelTapHitResponse> optionalBarrelHitResponse = barrelTapService.hitBarrelTap(id, value);
        return optionalBarrelHitResponse.map(barrelTapHitResponse -> new ResponseEntity<>(barrelTapHitResponse, HttpStatus.OK)).orElseThrow(HitException::new);
    }

    @ExceptionHandler(value = HitException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleHitBarrelTapException(HitException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Kranik nie istnieje lub pojemność beczki jest równa 0");
    }
}
