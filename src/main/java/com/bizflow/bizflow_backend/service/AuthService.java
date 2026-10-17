package com.bizflow.bizflow_backend.service;

import com.bizflow.bizflow_backend.constant.ErrorMessages;
import com.bizflow.bizflow_backend.constant.RoleConstants;
import com.bizflow.bizflow_backend.dto.auth.LoginRequest;
import com.bizflow.bizflow_backend.dto.auth.LoginResponse;
import com.bizflow.bizflow_backend.dto.auth.RegisterRequest;
import com.bizflow.bizflow_backend.dto.user.UserResponse;
import com.bizflow.bizflow_backend.entity.Role;
import com.bizflow.bizflow_backend.entity.User;
import com.bizflow.bizflow_backend.exception.ResourceNotFoundException;
import com.bizflow.bizflow_backend.repository.RoleRepository;
import com.bizflow.bizflow_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bizflow.bizflow_backend.exception.BadRequestException;
import com.bizflow.bizflow_backend.exception.UnauthorizedException;
import com.bizflow.bizflow_backend.exception.ResourceNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            UserService userService,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    ErrorMessages.EMAIL_ALREADY_REGISTERED
            );
        }
        Role defaultRole = roleRepository.findByName(RoleConstants.USER)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        ErrorMessages.DEFAULT_ROLE_NOT_FOUND
                )
        );

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setActive(true);

        user.setRoles(Set.of(defaultRole));

        User savedUser = userRepository.save(user);

        return userService.mapToUserResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException(ErrorMessages.INVALID_CREDENTIALS)
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            throw new UnauthorizedException(
                    ErrorMessages.INVALID_CREDENTIALS
            );
        }

        String token = jwtService.generateToken(user.getEmail());

        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return new LoginResponse(
                token,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                roles
        );
    }
}