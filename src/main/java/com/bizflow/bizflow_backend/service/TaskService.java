package com.bizflow.bizflow_backend.service;

import com.bizflow.bizflow_backend.dto.task.CreateTaskRequest;
import com.bizflow.bizflow_backend.dto.task.TaskResponse;
import com.bizflow.bizflow_backend.dto.task.UpdateTaskRequest;
import com.bizflow.bizflow_backend.entity.Task;
import com.bizflow.bizflow_backend.entity.User;
import com.bizflow.bizflow_backend.entity.Workflow;
import com.bizflow.bizflow_backend.enums.TaskPriority;
import com.bizflow.bizflow_backend.enums.TaskStatus;
import com.bizflow.bizflow_backend.exception.ResourceNotFoundException;
import com.bizflow.bizflow_backend.exception.UnauthorizedException;
import com.bizflow.bizflow_backend.repository.TaskRepository;
import com.bizflow.bizflow_backend.repository.UserRepository;
import com.bizflow.bizflow_backend.repository.WorkflowRepository;
import com.bizflow.bizflow_backend.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final WorkflowRepository workflowRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public TaskService(
            TaskRepository taskRepository,
            WorkflowRepository workflowRepository,
            UserRepository userRepository,
            SecurityUtils securityUtils
    ) {
        this.taskRepository = taskRepository;
        this.workflowRepository = workflowRepository;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public TaskResponse createTask(
            CreateTaskRequest request
    ) {

        Workflow workflow = workflowRepository
                .findById(request.getWorkflowId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Workflow not found")
                );

        User createdBy = securityUtils.getCurrentUser();

        User assignedTo = null;

        if (request.getAssignedToUserId() != null) {

            assignedTo = userRepository
                    .findById(request.getAssignedToUserId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Assigned user not found"
                            )
                    );
        }

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setWorkflow(workflow);
        task.setCreatedBy(createdBy);
        task.setAssignedTo(assignedTo);
        task.setDueDate(request.getDueDate());

        Task savedTask = taskRepository.save(task);

        return mapToTaskResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found")
                );

        return mapToTaskResponse(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByWorkflow(
            UUID workflowId
    ) {

        return taskRepository.findByWorkflowId(workflowId)
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksAssignedToUser(
            UUID userId
    ) {

        User currentUser =
                securityUtils.getCurrentUser();

        boolean isAdmin =
                securityUtils.hasRole("ADMIN");

        boolean isManager =
                securityUtils.hasRole("MANAGER");

        if (!isAdmin
                && !isManager
                && !currentUser.getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to view another user's tasks"
            );
        }

        return taskRepository
                .findByAssignedToId(userId)
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    @Transactional
    public TaskResponse updateTaskStatus(
            UUID taskId,
            TaskStatus status
    ) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found")
                );

        User currentUser =
                securityUtils.getCurrentUser();

        boolean isAdmin =
                securityUtils.hasRole("ADMIN");

        boolean isManager =
                securityUtils.hasRole("MANAGER");

        boolean isAssignedUser =
                task.getAssignedTo() != null
                        && task.getAssignedTo()
                        .getId()
                        .equals(currentUser.getId());

        if (!isAdmin && !isManager && !isAssignedUser) {

            throw new RuntimeException(
                    "You are not allowed to update this task"
            );
        }

        task.setStatus(status);

        Task updatedTask =
                taskRepository.save(task);

        return mapToTaskResponse(updatedTask);
    }

    @Transactional
    public TaskResponse assignTask(
            UUID taskId,
            UUID userId
    ) {

        if (!securityUtils.hasRole("ADMIN")
                && !securityUtils.hasRole("MANAGER")) {

            throw new RuntimeException(
                    "You are not allowed to assign tasks"
            );
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found")
                );

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        task.setAssignedTo(user);

        Task updatedTask =
                taskRepository.save(task);

        return mapToTaskResponse(updatedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks() {

        User currentUser =
                securityUtils.getCurrentUser();

        return taskRepository
                .findByAssignedToId(currentUser.getId())
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    @Transactional
    public TaskResponse updateTask(
            UUID taskId,
            UpdateTaskRequest request
    ) {

        if (!securityUtils.hasRole("ADMIN")
                && !securityUtils.hasRole("MANAGER")) {

            throw new UnauthorizedException(
                    "You are not allowed to update tasks"
            );
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found"
                        )
                );

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Task updatedTask =
                taskRepository.save(task);

        return mapToTaskResponse(updatedTask);
    }

    @Transactional
    public void deleteTask(UUID taskId) {

        if (!securityUtils.hasRole("ADMIN")
                && !securityUtils.hasRole("MANAGER")) {

            throw new UnauthorizedException(
                    "You are not allowed to delete tasks"
            );
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found"
                        )
                );

        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStatus(
            TaskStatus status
    ) {

        return taskRepository.findByStatus(status)
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByPriority(
            TaskPriority priority
    ) {

        return taskRepository.findByPriority(priority)
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    public TaskResponse mapToTaskResponse(Task task) {

        User assignedUser = task.getAssignedTo();

        String assignedName = null;
        UUID assignedUserId = null;

        if (assignedUser != null) {
            assignedUserId = assignedUser.getId();
            assignedName =
                    assignedUser.getFirstName()
                            + " "
                            + assignedUser.getLastName();
        }

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getWorkflow().getId(),
                task.getWorkflow().getName(),
                assignedUserId,
                assignedName,
                task.getCreatedBy().getId(),
                task.getCreatedBy().getFirstName()
                        + " "
                        + task.getCreatedBy().getLastName(),
                task.getDueDate(),
                task.getCreatedAt()
        );
    }
}
