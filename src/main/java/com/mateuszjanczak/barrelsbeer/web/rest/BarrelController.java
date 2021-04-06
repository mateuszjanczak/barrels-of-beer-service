package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelHitResponse;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelSetRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.ErrorResponse;
import com.mateuszjanczak.barrelsbeer.domain.entity.Barrel;
import com.mateuszjanczak.barrelsbeer.exception.HitException;
import com.mateuszjanczak.barrelsbeer.service.BarrelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class BarrelController {

    private final static String LIST_BARRELS = "/barrels";
    private final static String ADD_BARREL = "/barrels/add";
    private final static String GET_BARREL = "/barrels/{id}";
    private final static String SET_BARREL = "/barrels/{id}/set";
    private final static String HIT_BARREL = "/barrels/{id}/hit";

    private final BarrelService barrelService;

    public BarrelController(BarrelService barrelService) {
        this.barrelService = barrelService;
    }

    @GetMapping(GET_BARREL)
    ResponseEntity<Barrel> getBarrelById(@PathVariable int id) {
        return new ResponseEntity<>(barrelService.getBarrelById(id), HttpStatus.OK);
    }

    @GetMapping(LIST_BARRELS)
    ResponseEntity<List<Barrel>> getBarrelList(){
        return new ResponseEntity<>(barrelService.getBarrelList(), HttpStatus.OK);
    }

    @PostMapping(ADD_BARREL)
    ResponseEntity<?> addBarrel(@Valid @RequestBody BarrelAddRequest barrelAddRequest) {
        barrelService.addBarrel(barrelAddRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(SET_BARREL)
    ResponseEntity<?> setBarrel(@PathVariable int id, @Valid @RequestBody BarrelSetRequest barrelSetRequest) {
        barrelService.setBarrel(id, barrelSetRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(HIT_BARREL)
    ResponseEntity<BarrelHitResponse> hitBarrel(@PathVariable int id) {
        Optional<BarrelHitResponse> optionalBarrelHitResponse = barrelService.hit(id);
        return optionalBarrelHitResponse.map(barrelHitResponse -> new ResponseEntity<>(barrelHitResponse, HttpStatus.OK)).orElseThrow(HitException::new);
    }

    @ExceptionHandler(value = HitException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleHitException(HitException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Beczka nie istnieje lub jej pojemność jest równa 0");
    }
}
