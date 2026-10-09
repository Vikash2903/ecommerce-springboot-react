package com.ecommerce.user.service;

import com.ecommerce.user.dto.request.UserRequest;
import com.ecommerce.user.dto.response.UserResponse;

public interface UserService
{
	UserResponse register(UserRequest request);
	
	UserResponse getUserById(Long id); 
}
