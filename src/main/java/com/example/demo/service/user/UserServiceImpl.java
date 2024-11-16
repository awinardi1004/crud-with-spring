package com.example.demo.service.user;

import com.example.demo.model.Job;
import com.example.demo.model.User;
import com.example.demo.model.UserRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JobService jobService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByID(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public User updateUser(UserRequest request, Long id) {
        Optional<User> userData = getUserByID(id);

        if (userData.isEmpty()) {
            return null;
        }

        Optional<Job> jobData = jobService.getJobById(request.getJobId());

        if (jobData.isEmpty()) {
            return null;
        }

        Job job = jobData.get();
        User updatedUser = userData.get();

        updatedUser.setName(request.getName());
        updatedUser.setEmail(request.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(request.getPassword()));
        updatedUser.setJob(job);

        return userRepository.save(updatedUser);
    }

    @Override
    public boolean deleteUser(Long id) {
        Optional<User> userData = getUserByID(id);

        if (userData.isEmpty()) {
            return false;
        }

        userRepository.delete(userData.get());

        return true;
    }
}
