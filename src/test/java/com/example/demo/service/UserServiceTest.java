package com.example.demo.service;

import com.example.demo.model.Job;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.job.JobService;
import com.example.demo.service.user.UserService;
import com.example.demo.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobService jobService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, jobService, passwordEncoder);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User(1, new Job(1L, "job"), "user1", "user1@mail.com", "123", Role.USER);
        User user2 = new User(2, new Job(2L, "job"), "user2", "user2@mail.com", "123", Role.USER);

        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.getAllUsers();

        assertEquals(2, retrievedUsers.size());
        assertNotEquals(null, retrievedUsers);

    }

    @Test
    void testGetUserById() {
        User user1 = new User(1L,new Job(1L, "job"), "user1","user1@mail.com","123", Role.USER);

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        Optional<User> retrievedUser = userService.getUserByID(userId);

        assertEquals(user1, retrievedUser.get());
        assertEquals(user1.getID(), retrievedUser.get().getID());
    }

    @Test
    void testGetUserByEmail() {
        User user1 = new User(1L,new Job(1L, "job"), "user1","user1@mail.com","123", Role.USER);
        String email = "user1@mail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user1));

        User retrievedUser = userService.getUserByEmail(email);

        assertEquals(user1, retrievedUser);
        assertEquals(user1.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void testUpdateUser() {
        Job job1 = new Job(1L, "job");
        User user1 = new User(1L,new Job(1L, "job"), "user","user@mail.com","123", Role.USER);

        Long id = 1L;
        UserRequest request = new UserRequest("user","user@mail.com","123",1L);

        when(jobService.getJobById(1L)).thenReturn(Optional.of(job1));
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);

        User updatedUser = userService.updateUser(request,id);

        assertEquals(user1, updatedUser);
        assertEquals(user1.getName(), updatedUser.getName());
        assertEquals(user1.getEmail(), updatedUser.getEmail());
        assertEquals(user1.getPassword(), updatedUser.getPassword());
        assertEquals(user1.getJob().getID(), updatedUser.getJob().getID());
    }

    @Test
    void testDeleteUser() {
        User user1 = new User(1L,new Job(1L, "job"), "user","user@mail.com","123", Role.USER);
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        boolean isDeleted = userService.deleteUser(userId);

        assertTrue(isDeleted);
        verify(userRepository, times(1)).delete(any(User.class));
    }

}
