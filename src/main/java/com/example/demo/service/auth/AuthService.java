package com.example.demo.service.auth;

import com.example.demo.model.LoginRequest;
import com.example.demo.model.LoginResponse;
import com.example.demo.model.User;
import com.example.demo.model.UserRequest;


public interface AuthService {
    User register(UserRequest request);
    LoginResponse login(LoginRequest request);
}
