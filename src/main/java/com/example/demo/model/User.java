package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue
    private long ID;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    private String name;
    private String email;
    private String password;
}
