package com.bizflow.bizflow_backend.service;

import com.bizflow.bizflow_backend.dto.dashboard.DashboardResponse;
import com.bizflow.bizflow_backend.enums.TaskStatus;
import com.bizflow.bizflow_backend.exception.UnauthorizedException;
import com.bizflow.bizflow_backend.repository.DepartmentRepository;
import com.bizflow.bizflow_backend.repository.TaskRepository;
import com.bizflow.bizflow_backend.repository.UserRepository;
import com.bizflow.bizflow_backend.repository.WorkflowRepository;
import com.bizflow.bizflow_backend.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final WorkflowRepository workflowRepository;
    private final TaskRepository taskRepository;
    private final SecurityUtils securityUtils;

    public DashboardService(
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            WorkflowRepository workflowRepository,
            TaskRepository taskRepository,
            SecurityUtils securityUtils
    ) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.workflowRepository = workflowRepository;
        this.taskRepository = taskRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {

        if (!securityUtils.hasRole("ADMIN")
                && !securityUtils.hasRole("MANAGER")) {

            throw new UnauthorizedException(
                    "You are not allowed to view the dashboard"
            );
        }

        long totalUsers = userRepository.count();

        long activeUsers =
                userRepository.countByActiveTrue();

        long totalDepartments =
                departmentRepository.count();

        long totalWorkflows =
                workflowRepository.count();

        long totalTasks =
                taskRepository.count();

        long completedTasks =
                taskRepository.countByStatus(
                        TaskStatus.COMPLETED
                );

        long pendingTasks =
                totalTasks - completedTasks;

        return new DashboardResponse(
                totalUsers,
                activeUsers,
                totalDepartments,
                totalWorkflows,
                totalTasks,
                completedTasks,
                pendingTasks
        );
    }
}