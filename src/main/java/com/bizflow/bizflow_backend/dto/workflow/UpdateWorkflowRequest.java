package com.bizflow.bizflow_backend.dto.workflow;

import jakarta.validation.constraints.NotBlank;

public class UpdateWorkflowRequest {

    @NotBlank(message = "Workflow name is required")
    private String name;

    private String description;

    public UpdateWorkflowRequest() {
    }

    public UpdateWorkflowRequest(
            String name,
            String description
    ) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}