package com.mateuszjanczak.barrelsbeer.web.rest;

import com.mateuszjanczak.barrelsbeer.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/admin")
public class AdminController {

    private final static String RESET_DB = "/reset-db";
    private final static String TAP_ENABLE = "/barrelTaps/{id}/enable/{status}";

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(RESET_DB)
    public ResponseEntity<?> resetDB() {
        adminService.resetDB();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(TAP_ENABLE)
    public ResponseEntity<?> enableTap(@PathVariable int id, @PathVariable int status) {
        adminService.setTapEnabled(id, status == 1);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
