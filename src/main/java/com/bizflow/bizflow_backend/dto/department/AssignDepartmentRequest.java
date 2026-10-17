package com.bizflow.bizflow_backend.dto.department;

import java.util.UUID;

public class AssignDepartmentRequest {

    private UUID departmentId;

    public AssignDepartmentRequest() {
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }
}