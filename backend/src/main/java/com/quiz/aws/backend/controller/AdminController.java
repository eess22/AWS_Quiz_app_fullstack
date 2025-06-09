package com.quiz.aws.backend.controller;

import com.quiz.aws.backend.dto.UserDto;
import com.quiz.aws.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(adminService.getAllUsers(search));
    }

    @PutMapping("/user/{id}/role")
    public ResponseEntity<Void> changeUserRole(@PathVariable Long id) {
        adminService.toggleRole(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{id}/ban")
    public ResponseEntity<Void> toggleUserBan(@PathVariable Long id) {
        adminService.toggleBan(id);
        return ResponseEntity.ok().build();
    }
}