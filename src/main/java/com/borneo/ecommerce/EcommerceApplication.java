package com.borneo.ecommerce;

import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.repository.RoleRepository;
import com.borneo.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        return args -> {
            String[] roleNames = {"USER", "ADMIN", "VENDOR"};

            for (String roleName : roleNames) {
                if (roleRepository.findByName(roleName) == null) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }
            }
        };
    }
}
