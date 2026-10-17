package com.bizflow.bizflow_backend.controller;

import com.bizflow.bizflow_backend.dto.common.PageResponse;
import com.bizflow.bizflow_backend.dto.department.AssignDepartmentRequest;
import com.bizflow.bizflow_backend.dto.user.UpdateUserRequest;
import com.bizflow.bizflow_backend.dto.user.UpdateUserRolesRequest;
import com.bizflow.bizflow_backend.dto.user.UpdateUserStatusRequest;
import com.bizflow.bizflow_backend.dto.user.UserResponse;
import com.bizflow.bizflow_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getUsers(

            @RequestParam(required = false) String search,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "firstName") String sortBy,

            @RequestParam(defaultValue = "asc") String direction
    ) {

        return ResponseEntity.ok(
                userService.getUsers(
                        search,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    ) {

        return ResponseEntity.ok(
                userService.updateUser(id, request)
        );
    }

    // UPDATE USER ROLES
    @PutMapping("/{id}/roles")
    public ResponseEntity<UserResponse> updateUserRoles(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRolesRequest request
    ) {

        return ResponseEntity.ok(
                userService.updateUserRoles(id, request)
        );
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable UUID id
    ) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
    // Assign Department to User
    @PutMapping("/{userId}/department")
    public ResponseEntity<UserResponse> assignDepartment(
            @PathVariable UUID userId,
            @RequestBody AssignDepartmentRequest request
    ) {
        return ResponseEntity.ok(
                userService.assignDepartment(userId, request)
        );
    }
    // Remove User from Department
    @DeleteMapping("/{userId}/department")
    public ResponseEntity<UserResponse> removeDepartment(
            @PathVariable UUID userId
    ) {

        return ResponseEntity.ok(
                userService.removeDepartment(userId)
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(
            @PathVariable UUID id,
            @RequestBody UpdateUserStatusRequest request
    ) {
        return ResponseEntity.ok(
                userService.updateUserStatus(id, request)
        );
    }

}