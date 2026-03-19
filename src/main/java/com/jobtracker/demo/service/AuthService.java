package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.AuthResponse;
import com.jobtracker.demo.dto.LoginRequest;
import com.jobtracker.demo.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
