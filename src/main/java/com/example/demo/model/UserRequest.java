package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "name is required")
    private String name;

    @Email(message = "input must be valid email")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotNull(message = "job_id is requeired")
    @JsonProperty("job_id")
    private Long jobId;
}
