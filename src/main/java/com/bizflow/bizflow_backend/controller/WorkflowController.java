package com.bizflow.bizflow_backend.controller;

import com.bizflow.bizflow_backend.dto.workflow.CreateWorkflowRequest;
import com.bizflow.bizflow_backend.dto.workflow.UpdateWorkflowRequest;
import com.bizflow.bizflow_backend.dto.workflow.WorkflowResponse;
import com.bizflow.bizflow_backend.enums.WorkflowStatus;
import com.bizflow.bizflow_backend.service.WorkflowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(
            WorkflowService workflowService
    ) {
        this.workflowService = workflowService;
    }

    @PostMapping
    public ResponseEntity<WorkflowResponse> createWorkflow(
            @Valid @RequestBody CreateWorkflowRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workflowService.createWorkflow(request));
    }

    @GetMapping
    public ResponseEntity<List<WorkflowResponse>>
    getAllWorkflows() {

        return ResponseEntity.ok(
                workflowService.getAllWorkflows()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowResponse> getWorkflowById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                workflowService.getWorkflowById(id)
        );
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<WorkflowResponse>>
    getWorkflowsByDepartment(
            @PathVariable UUID departmentId
    ) {

        return ResponseEntity.ok(
                workflowService.getWorkflowsByDepartment(
                        departmentId
                )
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WorkflowResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam WorkflowStatus status
    ) {

        return ResponseEntity.ok(
                workflowService.updateWorkflowStatus(id, status)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkflowResponse> updateWorkflow(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateWorkflowRequest request
    ) {

        return ResponseEntity.ok(
                workflowService.updateWorkflow(id, request)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<WorkflowResponse>>
    getWorkflowsByStatus(
            @PathVariable WorkflowStatus status
    ) {

        return ResponseEntity.ok(
                workflowService.getWorkflowsByStatus(status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(
            @PathVariable UUID id
    ) {

        workflowService.deleteWorkflow(id);

        return ResponseEntity.noContent().build();
    }
}