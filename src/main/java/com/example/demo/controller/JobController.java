package com.example.demo.controller;

import com.example.demo.model.Job;
import com.example.demo.model.JobRequest;
import com.example.demo.model.Response;
import com.example.demo.service.job.JobService;
import com.example.demo.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<Response<List<Job>>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        Response<List<Job>> response = new Response<>("all jobs", jobs);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response<Job>> createJob(@Valid @RequestBody JobRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String validationErrors = Util.getValidationErrors(bindingResult);

            return new ResponseEntity<>(new Response<>(validationErrors,null), HttpStatus.BAD_REQUEST);
        }
        Job job = jobService.createJob(request);
        Response<Job> response = new Response<>("job created",job);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}