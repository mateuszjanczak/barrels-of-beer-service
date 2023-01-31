package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.domain.enums.TableType;
import com.mateuszjanczak.barrelsbeer.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/admin")
public class AdminController {

    private final static String RESET_DATABASE = "/resetDatabase/{table}";
    private final static String TAP_ENABLE = "/barrelTaps/{id}/enable/{status}";
    private final static String TAP_RESET = "/barrelTaps/{id}/reset";

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(RESET_DATABASE)
    public ResponseEntity<?> resetDatabase(@PathVariable TableType table) {
        adminService.resetDatabase(table);
        return new ResponseEntity<>(OK);
    }

    @PostMapping(TAP_ENABLE)
    public ResponseEntity<?> enableTap(@PathVariable int id, @PathVariable int status) {
        adminService.setTapEnabled(id, status == 1);
        return new ResponseEntity<>(OK);
    }

    @PostMapping(TAP_RESET)
    public ResponseEntity<Void> resetTap(@PathVariable int id){
        adminService.resetTap(id);
        return new ResponseEntity<>(OK);
    }
}
