package com.bizflow.bizflow_backend.dto.dashboard;

public class DashboardResponse {

    private long totalUsers;
    private long activeUsers;
    private long totalDepartments;
    private long totalWorkflows;
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;

    public DashboardResponse(
            long totalUsers,
            long activeUsers,
            long totalDepartments,
            long totalWorkflows,
            long totalTasks,
            long completedTasks,
            long pendingTasks
    ) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.totalDepartments = totalDepartments;
        this.totalWorkflows = totalWorkflows;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public long getTotalDepartments() {
        return totalDepartments;
    }

    public long getTotalWorkflows() {
        return totalWorkflows;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public long getPendingTasks() {
        return pendingTasks;
    }
}