package com.example.demo.service.user;

import com.example.demo.model.User;
import com.example.demo.model.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserByID(Long id);
    User getUserByEmail(String email);
    User updateUser(UserRequest request, Long id);
    boolean deleteUser(Long id);
}
