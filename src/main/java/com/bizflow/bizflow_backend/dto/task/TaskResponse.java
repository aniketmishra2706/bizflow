package com.bizflow.bizflow_backend.dto.task;

import com.bizflow.bizflow_backend.enums.TaskPriority;
import com.bizflow.bizflow_backend.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskResponse {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;

    private UUID workflowId;
    private String workflowName;

    private UUID assignedToUserId;
    private String assignedToName;

    private UUID createdByUserId;
    private String createdByName;

    private LocalDateTime dueDate;
    private LocalDateTime createdAt;

    public TaskResponse(
            UUID id,
            String title,
            String description,
            TaskStatus status,
            TaskPriority priority,
            UUID workflowId,
            String workflowName,
            UUID assignedToUserId,
            String assignedToName,
            UUID createdByUserId,
            String createdByName,
            LocalDateTime dueDate,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        this.assignedToUserId = assignedToUserId;
        this.assignedToName = assignedToName;
        this.createdByUserId = createdByUserId;
        this.createdByName = createdByName;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public TaskStatus getStatus() {
        return status;
    }
    public TaskPriority getPriority() {
        return priority;
    }
    public UUID getWorkflowId() {
        return workflowId;
    }
    public String getWorkflowName() {
        return workflowName;
    }
    public UUID getAssignedToUserId() {
        return assignedToUserId;
    }
    public String getAssignedToName() {
        return assignedToName;
    }
    public UUID getCreatedByUserId() { 
        return createdByUserId;
    }
    public String getCreatedByName() {
        return createdByName;
    }
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}