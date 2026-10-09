package com.ecommerce.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.user.dto.request.UserRequest;
import com.ecommerce.user.dto.response.UserResponse;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserRole;
import com.ecommerce.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService
{

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) 
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(UserRequest request) 
    {
        String email = request.getEmail().trim().toLowerCase();

        log.info("Registering new user with email: {}", email);

        if (userRepository.existsByEmailIgnoreCase(email)) 
        {
            log.warn("Registration failed. Email already exists: {}", email);

            throw new DuplicateResourceException("Email is already registered");
        }

        User user = new User();

        user.setName(request.getName().trim());
        user.setEmail(email);

        // Never store the plain-text password.
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Public registration always creates CUSTOMER.
        user.setRole(UserRole.CUSTOMER);

        User savedUser = userRepository.save(user);

        log.info("User registered successfully. User ID: {}",savedUser.getId());

        return mapToResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) 
    {
        log.info("Fetching user with id: {}", id);

        User user = userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) 
    {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}