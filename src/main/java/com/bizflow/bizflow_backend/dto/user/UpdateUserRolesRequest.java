package com.bizflow.bizflow_backend.dto.user;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class UpdateUserRolesRequest {

    @NotEmpty(message = "At least one role is required")
    private Set<String> roles;

    public UpdateUserRolesRequest() {
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}