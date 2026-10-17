package com.bizflow.bizflow_backend.service;

import com.bizflow.bizflow_backend.dto.department.CreateDepartmentRequest;
import com.bizflow.bizflow_backend.dto.department.DepartmentResponse;
import com.bizflow.bizflow_backend.dto.department.UpdateDepartmentRequest;
import com.bizflow.bizflow_backend.dto.user.UserResponse;
import com.bizflow.bizflow_backend.entity.Department;
import com.bizflow.bizflow_backend.exception.BadRequestException;
import com.bizflow.bizflow_backend.exception.ResourceNotFoundException;
import com.bizflow.bizflow_backend.repository.DepartmentRepository;
import com.bizflow.bizflow_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    public DepartmentService(
            DepartmentRepository departmentRepository,
             UserRepository userRepository,
            UserService userService
    ) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public DepartmentResponse createDepartment(
            CreateDepartmentRequest request
    ) {

        if (departmentRepository.existsByName(request.getName())) {
            throw new BadRequestException(
                    "Department already exists"
            );
        }

        Department department = new Department();

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        Department savedDepartment =
                departmentRepository.save(department);

        return mapToResponse(savedDepartment);
    }

    @Transactional
    public DepartmentResponse updateDepartment(
            UUID id,
            UpdateDepartmentRequest request
    ) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found"
                        )
                );

        if (!department.getName().equals(request.getName())
                && departmentRepository.existsByName(request.getName())) {

            throw new BadRequestException(
                    "Department name already exists"
            );
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        return mapToResponse(department);
    }

    @Transactional
    public void deleteDepartment(UUID id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found"
                        )
                );

        departmentRepository.delete(department);
    }

    @Transactional
    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public DepartmentResponse getDepartmentById(UUID id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found"
                        )
                );

        return mapToResponse(department);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByDepartment(UUID departmentId) {

        departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found")
                );

        return userRepository.findByDepartmentId(departmentId)
                .stream()
                .map(userService::mapToUserResponse)
                .toList();
    }

    private DepartmentResponse mapToResponse(
            Department department
    ) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getCreatedAt()
        );
    }
}