package com.bizflow.bizflow_backend.initializer;

import com.bizflow.bizflow_backend.constant.RoleConstants;
import com.bizflow.bizflow_backend.entity.Role;
import com.bizflow.bizflow_backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        createRoleIfNotExists(RoleConstants.USER);
        createRoleIfNotExists(RoleConstants.ADMIN);
    }

    private void createRoleIfNotExists(String roleName) {

        if (!roleRepository.existsByName(roleName)) {

            Role role = new Role();
            role.setName(roleName);

            roleRepository.save(role);
        }
    }

}
