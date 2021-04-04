package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.entity.Barrel;
import com.mateuszjanczak.barrelsbeer.service.BarrelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class BarrelController {

    private final static String LIST_BARRELS = "/barrels";
    private final static String ADD_BARREL = "/barrels/add";

    private final BarrelService barrelService;

    public BarrelController(BarrelService barrelService) {
        this.barrelService = barrelService;
    }

    @GetMapping(LIST_BARRELS)
    ResponseEntity<List<Barrel>> getBarrelList(){
        return new ResponseEntity<>(barrelService.getBarrelList(), HttpStatus.OK);
    }

    @PostMapping(ADD_BARREL)
    ResponseEntity<?> addBarrel(@RequestBody BarrelAddRequest barrelAddRequest) {
        barrelService.addBarrel(barrelAddRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
