package com.bizflow.bizflow_backend.repository;

import com.bizflow.bizflow_backend.entity.Workflow;
import com.bizflow.bizflow_backend.enums.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowRepository
        extends JpaRepository<Workflow, UUID> {

    List<Workflow> findByDepartmentId(UUID departmentId);

    List<Workflow> findByStatus(WorkflowStatus status);
}