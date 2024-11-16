package com.example.demo.controller;

import com.example.demo.model.Response;
import com.example.demo.model.User;
import com.example.demo.model.UserRequest;
import com.example.demo.model.UserResponse;
import com.example.demo.service.user.UserService;
import com.example.demo.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response<List<UserResponse>>> getAllUser() {
        List<User> users = userService.getAllUsers();

        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::convertToUserResponse)
                .toList();

        Response<List<UserResponse>> response = new Response<>(
                "all users",
                userResponses
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserResponse>> getById(@PathVariable("id") Long id) {
        Optional<User> userData = userService.getUserByID(id);

        if (userData.isEmpty()) {
            return new ResponseEntity<>(new Response<>("user not found",null), HttpStatus.NOT_FOUND);
        }

        Response<UserResponse> response = new Response<>("user found", UserResponse.convertToUserResponse(userData.get()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<UserResponse>> update(@PathVariable("id") Long id, @Valid @RequestBody UserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String validationErrors = Util.getValidationErrors(bindingResult);

            return new ResponseEntity<>(new Response<>(validationErrors,null), HttpStatus.BAD_REQUEST);
        }

        User user = userService.updateUser(request, id);

        if (user == null) {
            return new ResponseEntity<>(new Response<>("update user failed",null), HttpStatus.BAD_REQUEST);
        }

        Response<UserResponse> response = new Response<>("user updated", UserResponse.convertToUserResponse(user));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Boolean>> delete(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User currentUser = userService.getUserByEmail(userDetails.getUsername());

        // Check if the user is attempting to delete their own account
        if (currentUser.getID() == id) {
            return new ResponseEntity<>(new Response<>("cannot delete your own account", false), HttpStatus.BAD_REQUEST);
        }

        boolean isDeleted = userService.deleteUser(id);

        if (!isDeleted) {
            return new ResponseEntity<>(new Response<>("delete user failed",false), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new Response<>("user deleted",true),HttpStatus.OK);
    }
}
