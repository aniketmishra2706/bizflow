package com.bizflow.bizflow_backend.dto.workflow;

import com.bizflow.bizflow_backend.enums.WorkflowStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class WorkflowResponse {

    private UUID id;
    private String name;
    private String description;
    private WorkflowStatus status;

    private UUID departmentId;
    private String departmentName;

    private UUID createdByUserId;
    private String createdByName;

    private LocalDateTime createdAt;

    public WorkflowResponse(
            UUID id,
            String name,
            String description,
            WorkflowStatus status,
            UUID departmentId,
            String departmentName,
            UUID createdByUserId,
            String createdByName,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.createdByUserId = createdByUserId;
        this.createdByName = createdByName;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public WorkflowStatus getStatus() {
        return status;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public UUID getCreatedByUserId() {
        return createdByUserId;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}