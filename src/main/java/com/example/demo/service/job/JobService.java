package com.example.demo.service.job;

import com.example.demo.model.Job;
import com.example.demo.model.JobRequest;

import java.util.List;
import java.util.Optional;

public interface JobService {
    List<Job> getAllJobs();
    Optional<Job> getJobById(Long id);
    Job createJob(JobRequest request);
}
