package com.example.demo.service.auth;

import com.example.demo.config.JwtService;
import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JobService jonService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public User register(UserRequest request) {
        Optional<Job> jobData = jonService.getJobById(request.getJobId());

        if (jobData.isEmpty()) {
            return null;
        }

        Job job = jobData.get();

        User userData = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .job(job)
                .build();

        return userRepository.save(userData);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
