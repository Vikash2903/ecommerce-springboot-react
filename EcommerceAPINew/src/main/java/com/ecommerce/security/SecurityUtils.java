package com.ecommerce.security;

import org.springframework.security.core.Authentication;

public final class SecurityUtils 
{
    public static UserPrincipal getCurrentUser(Authentication authentication) 
    {
        if (authentication == null || !authentication.isAuthenticated()) 
        {
            throw new SecurityException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserPrincipal)) 
        {
            throw new SecurityException("Invalid authenticated user");
        }

        return (UserPrincipal) principal;
    }

    public static Long getCurrentUserId(Authentication authentication) 
    {
        return getCurrentUser(authentication).getId();
    }

    public static String getCurrentUserEmail(Authentication authentication) 
    {
        return getCurrentUser(authentication).getEmail();
    }
}