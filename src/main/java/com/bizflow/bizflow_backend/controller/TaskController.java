package com.bizflow.bizflow_backend.controller;

import com.bizflow.bizflow_backend.dto.task.CreateTaskRequest;
import com.bizflow.bizflow_backend.dto.task.TaskResponse;
import com.bizflow.bizflow_backend.dto.task.UpdateTaskRequest;
import com.bizflow.bizflow_backend.enums.TaskPriority;
import com.bizflow.bizflow_backend.enums.TaskStatus;
import com.bizflow.bizflow_backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    // Create task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(request));
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {

        return ResponseEntity.ok(
                taskService.getAllTasks()
        );
    }

    // Get one task
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                taskService.getTaskById(id)
        );
    }

    // Get workflow tasks
    @GetMapping("/workflow/{workflowId}")
    public ResponseEntity<List<TaskResponse>> getTasksByWorkflow(
            @PathVariable UUID workflowId
    ) {

        return ResponseEntity.ok(
                taskService.getTasksByWorkflow(workflowId)
        );
    }

    // Get user's assigned tasks
    @GetMapping("/assigned/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksAssignedToUser(
            @PathVariable UUID userId
    ) {

        return ResponseEntity.ok(
                taskService.getTasksAssignedToUser(userId)
        );
    }

    // Update status
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable UUID id,
            @RequestParam TaskStatus status
    ) {

        return ResponseEntity.ok(
                taskService.updateTaskStatus(id, status)
        );
    }

    // Assign task to user
    @PatchMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable UUID taskId,
            @PathVariable UUID userId
    ) {

        return ResponseEntity.ok(
                taskService.assignTask(taskId, userId)
        );
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskResponse>> getMyTasks() {

        return ResponseEntity.ok(
                taskService.getMyTasks()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {

        return ResponseEntity.ok(
                taskService.updateTask(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID id
    ) {

        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>>
    getTasksByStatus(
            @PathVariable TaskStatus status
    ) {

        return ResponseEntity.ok(
                taskService.getTasksByStatus(status)
        );
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskResponse>>
    getTasksByPriority(
            @PathVariable TaskPriority priority
    ) {

        return ResponseEntity.ok(
                taskService.getTasksByPriority(priority)
        );
    }



}