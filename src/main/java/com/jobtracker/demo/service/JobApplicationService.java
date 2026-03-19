package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.JobRequest;
import com.jobtracker.demo.dto.JobResponse;
import com.jobtracker.demo.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface JobApplicationService {
    JobResponse create(JobRequest request, String email);
    Page<JobResponse> getAll(String email, JobStatus status, LocalDate from, LocalDate to, Pageable pageable);
    JobResponse update(Long id, JobRequest request, String email);
    void delete(Long id, String email);
    JobResponse uploadResume(Long id, MultipartFile file, String email);
}
