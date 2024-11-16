package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<Response<UserResponse>> register(@Valid @RequestBody UserRequest request) {
        User user = service.register(request);
        Response<UserResponse> response = new Response<>("user created", UserResponse.convertToUserResponse(user));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse login = service.login(request);
        Response<String> response = new Response<>("login succses", login.getToken());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
