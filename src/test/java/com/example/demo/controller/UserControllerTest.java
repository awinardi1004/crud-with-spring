package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController controller;

    @Mock
    private UserService service;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UserController(service);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User(1,new Job(1L, "job"), "user1","user1@mail.com","123", Role.USER);
        User user2 = new User(2,new Job(2L, "job"), "user2","user2@mail.com","123", Role.USER);

        List<User> users = List.of(user1,user2);
        List<UserResponse> userResponses = users.stream().map(UserResponse::convertToUserResponse).toList();

        when(service.getAllUsers()).thenReturn(users);


        ResponseEntity<Response<List<UserResponse>>> response = controller.getAllUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("all users", response.getBody().getMessage());
        assertEquals(userResponses, response.getBody().getData());
    }

    @Test
    void testGetUserById() {
        User user1 = new User(1L,new Job(1L, "job"), "user1","user1@mail.com","123", Role.USER);
        UserResponse userResponse = UserResponse.convertToUserResponse(user1);

        when(service.getUserByID(1L)).thenReturn(Optional.of(user1));

        ResponseEntity<Response<UserResponse>> response = controller.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user found", response.getBody().getMessage());
        assertEquals(userResponse, response.getBody().getData());
    }

    @Test
    void testUpdateUser() {
        Job job1 = new Job(1L, "job");
        User user1 = new User(1L,job1, "user","user@mail.com","123", Role.USER);

        Long id = 1L;
        UserRequest request = new UserRequest("user","user@mail.com","123",1L);
        UserResponse userResponse = UserResponse.convertToUserResponse(user1);
        BindingResult bindingResult = mock(BindingResult.class);

        when(service.updateUser(request, id)).thenReturn(user1);

        ResponseEntity<Response<UserResponse>> response = controller.update(id, request, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user updated", response.getBody().getMessage());
        assertEquals(userResponse, response.getBody().getData());
    }

    @Test
    void testDeleteUser() {
        Long id = 2L;

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getUsername()).thenReturn("admin@mail.com");
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(service.getUserByEmail("admin@mail.com")).thenReturn(new User(1L, new Job(1L,"job"),"admin","admin@mail.com","123",Role.ADMIN));

        when(service.deleteUser(id)).thenReturn(true);

        ResponseEntity<Response<Boolean>> response = controller.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user deleted", response.getBody().getMessage());
        assertTrue(response.getBody().getData());
    }
}
