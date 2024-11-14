package com.example.demo.service.user;

import com.example.demo.model.User;
import com.example.demo.model.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getByID(Long id);
    User create(UserRequest request);
    User update(UserRequest request, Long id);
    boolean delete(Long id);
}
