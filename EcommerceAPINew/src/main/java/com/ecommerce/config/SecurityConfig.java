package com.ecommerce.config;

import com.ecommerce.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    private final UserDetailsService userDetailsService;
    
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) 
    {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    
        this.userDetailsService = userDetailsService;
        
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception 
    {
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register","/api/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**").permitAll()

                        // Product browsing can be public.
                        .requestMatchers(org.springframework.http.HttpMethod.GET,"/api/products/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET,"/api/categories/**").permitAll()
                        
                        .requestMatchers("/api/cart/**").hasRole("CUSTOMER")
                        
                        .requestMatchers("/api/orders/**").hasRole("CUSTOMER")
                        
                        .requestMatchers("/api/payments/**").hasRole("CUSTOMER")
                        
                        // Admin operations.
                        .requestMatchers("/api/products/**", "/api/categories/**", "/api/admin/orders/**").hasRole("ADMIN")
                        
                        .anyRequest().authenticated()
                )
                
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() 
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception 
    {
        return configuration.getAuthenticationManager();
    }
}