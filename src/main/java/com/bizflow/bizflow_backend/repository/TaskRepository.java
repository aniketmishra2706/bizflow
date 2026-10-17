package com.bizflow.bizflow_backend.repository;

import com.bizflow.bizflow_backend.entity.Task;
import com.bizflow.bizflow_backend.enums.TaskPriority;
import com.bizflow.bizflow_backend.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByWorkflowId(UUID workflowId);

    List<Task> findByAssignedToId(UUID userId);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    long countByStatus(TaskStatus status);


}