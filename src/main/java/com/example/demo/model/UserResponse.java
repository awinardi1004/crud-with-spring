package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private long ID;
    private Job job;
    private String name;
    private String email;
    private Role role;

    public static UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .ID(user.getID())
                .job(user.getJob())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
