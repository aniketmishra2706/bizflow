package com.bizflow.bizflow_backend.service;

import com.bizflow.bizflow_backend.dto.workflow.CreateWorkflowRequest;
import com.bizflow.bizflow_backend.dto.workflow.UpdateWorkflowRequest;
import com.bizflow.bizflow_backend.dto.workflow.WorkflowResponse;
import com.bizflow.bizflow_backend.entity.Department;
import com.bizflow.bizflow_backend.entity.User;
import com.bizflow.bizflow_backend.entity.Workflow;
import com.bizflow.bizflow_backend.enums.WorkflowStatus;
import com.bizflow.bizflow_backend.exception.ResourceNotFoundException;
import com.bizflow.bizflow_backend.exception.UnauthorizedException;
import com.bizflow.bizflow_backend.repository.DepartmentRepository;
import com.bizflow.bizflow_backend.repository.WorkflowRepository;
import com.bizflow.bizflow_backend.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final DepartmentRepository departmentRepository;
    private final SecurityUtils securityUtils;

    public WorkflowService(
            WorkflowRepository workflowRepository,
            DepartmentRepository departmentRepository,
            SecurityUtils securityUtils
    ) {
        this.workflowRepository = workflowRepository;
        this.departmentRepository = departmentRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public WorkflowResponse createWorkflow(
            CreateWorkflowRequest request
    ) {

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found")
                );

        User createdBy = securityUtils.getCurrentUser();

        Workflow workflow = new Workflow();

        workflow.setName(request.getName());
        workflow.setDescription(request.getDescription());
        workflow.setDepartment(department);
        workflow.setCreatedBy(createdBy);
        workflow.setStatus(WorkflowStatus.DRAFT);

        Workflow savedWorkflow =
                workflowRepository.save(workflow);

        return mapToWorkflowResponse(savedWorkflow);
    }

    @Transactional(readOnly = true)
    public List<WorkflowResponse> getAllWorkflows() {

        return workflowRepository.findAll()
                .stream()
                .map(this::mapToWorkflowResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkflowResponse getWorkflowById(UUID id) {

        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Workflow not found")
                );

        return mapToWorkflowResponse(workflow);
    }

    @Transactional(readOnly = true)
    public List<WorkflowResponse> getWorkflowsByDepartment(
            UUID departmentId
    ) {

        return workflowRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToWorkflowResponse)
                .toList();
    }

    @Transactional
    public WorkflowResponse updateWorkflow(
            UUID id,
            UpdateWorkflowRequest request
    ) {

        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workflow not found"
                        )
                );

        if (!securityUtils.hasRole("ADMIN")
                && !securityUtils.hasRole("MANAGER")) {

            throw new UnauthorizedException(
                    "You are not allowed to update workflows"
            );
        }

        workflow.setName(request.getName());
        workflow.setDescription(request.getDescription());

        Workflow updatedWorkflow =
                workflowRepository.save(workflow);

        return mapToWorkflowResponse(updatedWorkflow);
    }

    @Transactional
    public WorkflowResponse updateWorkflowStatus(
            UUID id,
            WorkflowStatus status
    ) {

        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workflow not found"
                        )
                );

        if (!securityUtils.hasRole("ADMIN")
                && !securityUtils.hasRole("MANAGER")) {

            throw new UnauthorizedException(
                    "You are not allowed to update workflow status"
            );
        }

        workflow.setStatus(status);

        Workflow updatedWorkflow =
                workflowRepository.save(workflow);

        return mapToWorkflowResponse(updatedWorkflow);
    }

    @Transactional
    public void deleteWorkflow(UUID id) {

        if (!securityUtils.hasRole("ADMIN")
                && !securityUtils.hasRole("MANAGER")) {

            throw new UnauthorizedException(
                    "You are not allowed to delete workflows"
            );
        }

        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workflow not found"
                        )
                );

        workflowRepository.delete(workflow);
    }

    @Transactional(readOnly = true)
    public List<WorkflowResponse> getWorkflowsByStatus(
            WorkflowStatus status
    ) {

        return workflowRepository.findByStatus(status)
                .stream()
                .map(this::mapToWorkflowResponse)
                .toList();
    }




    public WorkflowResponse mapToWorkflowResponse(
            Workflow workflow
    ) {

        return new WorkflowResponse(
                workflow.getId(),
                workflow.getName(),
                workflow.getDescription(),
                workflow.getStatus(),
                workflow.getDepartment().getId(),
                workflow.getDepartment().getName(),
                workflow.getCreatedBy().getId(),
                workflow.getCreatedBy().getFirstName()
                        + " "
                        + workflow.getCreatedBy().getLastName(),
                workflow.getCreatedAt()
        );
    }
}
