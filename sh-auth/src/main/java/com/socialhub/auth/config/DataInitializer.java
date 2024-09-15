package com.socialhub.auth.config;

import com.socialhub.auth.entity.Role;
import com.socialhub.auth.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        if (!roleRepository.findByName("ROLE_USER").isPresent()) {
            roleRepository.save(Role.builder().name("ROLE_USER").build());
        }

        if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        }
    }
}
