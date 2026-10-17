package com.bizflow.bizflow_backend.config;

import com.bizflow.bizflow_backend.entity.Role;
import com.bizflow.bizflow_backend.entity.User;
import com.bizflow.bizflow_backend.repository.RoleRepository;
import com.bizflow.bizflow_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createDefaultAdmin(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String adminEmail = "admin@bizflow.com";

            // Do nothing if admin already exists
            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }

            Role adminRole = roleRepository
                    .findByName("ADMIN")
                    .orElseThrow(() ->
                            new RuntimeException("ADMIN role not found")
                    );

            User admin = new User();

            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setEmail(adminEmail);
            admin.setPasswordHash(
                    passwordEncoder.encode("Admin@123")
            );
            admin.setActive(true);

            // ONLY ADMIN
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
        };
    }

}