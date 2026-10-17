package com.bizflow.bizflow_backend.controller;

import com.bizflow.bizflow_backend.dto.user.UpdateUserRolesRequest;
import com.bizflow.bizflow_backend.dto.user.UserResponse;
import com.bizflow.bizflow_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/users/{id}/roles")
    public ResponseEntity<UserResponse> updateUserRoles(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRolesRequest request
    ) {

        return ResponseEntity.ok(
                userService.updateUserRoles(id, request)
        );
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {

        return ResponseEntity.ok("Welcome to the Admin Dashboard");
    }
}