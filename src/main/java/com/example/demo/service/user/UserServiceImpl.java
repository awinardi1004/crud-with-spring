package com.example.demo.service.user;

import com.example.demo.model.Job;
import com.example.demo.model.User;
import com.example.demo.model.UserRequest;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JobService jonService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getByID(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        //TODO: implement this
        return Optional.empty();
    }

    @Override
    public User create(UserRequest request) {
        Optional<Job> jobData = jonService.getJobById(request.getJobId());

        if (jobData.isEmpty()) {
            return null;
        }

        Job job = jobData.get();

        User userData = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .job(job)
                .build();

        return userRepository.save(userData);
    }

    @Override
    public User update(UserRequest request, Long id) {
        Optional<User> userData = getByID(id);

        if (userData.isEmpty()) {
            return null;
        }

        Optional<Job> jobData = jonService.getJobById(request.getJobId());

        if (jobData.isEmpty()) {
            return null;
        }

        Job job = jobData.get();
        User updatedUser = userData.get();

        updatedUser.setName(request.getName());
        updatedUser.setEmail(request.getEmail());
        updatedUser.setPassword(request.getPassword());
        updatedUser.setJob(job);

        return userRepository.save(updatedUser);
    }

    @Override
    public boolean delete(Long id) {
        Optional<User> userData = getByID(id);

        if (userData.isEmpty()) {
            return false;
        }

        userRepository.delete(userData.get());

        return true;
    }
}
