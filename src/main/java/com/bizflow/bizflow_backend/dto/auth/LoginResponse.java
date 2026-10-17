package com.bizflow.bizflow_backend.dto.auth;

import java.util.Set;
import java.util.UUID;

public class LoginResponse {

    private String accessToken;
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;

    public LoginResponse() {
    }

    public LoginResponse(
            String accessToken,
            UUID id,
            String firstName,
            String lastName,
            String email,
            Set<String> roles
    ) {
        this.accessToken =  accessToken;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}