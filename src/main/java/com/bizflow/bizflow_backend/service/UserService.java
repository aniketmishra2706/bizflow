package com.bizflow.bizflow_backend.service;

import com.bizflow.bizflow_backend.dto.common.PageResponse;
import com.bizflow.bizflow_backend.dto.department.AssignDepartmentRequest;
import com.bizflow.bizflow_backend.dto.user.UpdateUserRequest;
import com.bizflow.bizflow_backend.dto.user.UpdateUserRolesRequest;
import com.bizflow.bizflow_backend.dto.user.UpdateUserStatusRequest;
import com.bizflow.bizflow_backend.dto.user.UserResponse;
import com.bizflow.bizflow_backend.entity.Department;
import com.bizflow.bizflow_backend.entity.User;
import com.bizflow.bizflow_backend.exception.BadRequestException;
import com.bizflow.bizflow_backend.exception.ResourceNotFoundException;
import com.bizflow.bizflow_backend.repository.DepartmentRepository;
import com.bizflow.bizflow_backend.repository.RoleRepository;
import com.bizflow.bizflow_backend.entity.Role;
import java.util.stream.Collectors;
import com.bizflow.bizflow_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bizflow.bizflow_backend.dto.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            DepartmentRepository departmentRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // GET ALL USERS
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // GET USER BY ID
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        return mapToUserResponse(user);
    }

    // UPDATE USER
    @Transactional
    public UserResponse updateUser(
            UUID id,
            UpdateUserRequest request
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        // Check if another user already uses this email
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new BadRequestException("Email is already in use");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public UserResponse updateUserStatus(
            UUID id,
            UpdateUserStatusRequest request
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        user.setActive(request.isActive());

        User updatedUser = userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse updateUserRoles(
            UUID userId,
            UpdateUserRolesRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Set<Role> roles = request.getRoles()
                .stream()
                .map(roleName ->
                        roleRepository.findByName(roleName)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Role not found: " + roleName
                                        )
                                )
                )
                .collect(Collectors.toSet());

        user.setRoles(roles);

        User updatedUser = userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        userRepository.delete(user);
    }

    @Transactional
    public UserResponse assignDepartment(
            UUID userId,
            AssignDepartmentRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found")
                );

        user.setDepartment(department);

        User updatedUser = userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse removeDepartment(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        user.setDepartment(null);

        User updatedUser = userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }

    public Optional<User> findByEmailWithRoles(String email) {
        return userRepository.findByEmailWithRoles(email);
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getUsers(
            String search,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable =
                PageRequest.of(page, size, sort);

        Page<User> userPage;

        if (search != null && !search.isBlank()) {

            userPage = userRepository.searchUsers(
                    search,
                    pageable
            );

        } else {

            userPage = userRepository.findAll(pageable);
        }

        List<UserResponse> users =
                userPage.getContent()
                        .stream()
                        .map(this::mapToUserResponse)
                        .toList();

        return new PageResponse<>(
                users,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }


    // CONVERT ENTITY TO RESPONSE DTO
    public UserResponse mapToUserResponse(User user) {

        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        String departmentName = null;

        if (user.getDepartment() != null) {
            departmentName = user.getDepartment().getName();
        }

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive(),
                departmentName,
                user.getCreatedAt(),
                roles
        );
    }
}