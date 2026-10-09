package com.ecommerce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserRole;
import com.ecommerce.user.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner
{
    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.name:Admin}")
    private String adminName;

    @Value("${app.admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) 
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) 
    {
        String email = adminEmail.trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) 
        {
            log.info("Admin user already exists: {}", email);
            return;
        }

        User admin = new User();

        admin.setName(adminName);
        admin.setEmail(email);

        admin.setPassword(passwordEncoder.encode(adminPassword));

        admin.setRole(UserRole.ADMIN);

        userRepository.save(admin);

        log.info("Default admin user created: {}", email);
    }
}