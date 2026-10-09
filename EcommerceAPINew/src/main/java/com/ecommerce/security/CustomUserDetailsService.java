package com.ecommerce.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService 
{
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException 
    {
        User user = userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new UsernameNotFoundException("User with email "+email+" not found"));

        return new UserPrincipal(user);
    }
}