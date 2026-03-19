package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.JobRequest;
import com.jobtracker.demo.dto.JobResponse;
import com.jobtracker.demo.entity.JobApplication;
import com.jobtracker.demo.entity.JobStatus;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.exception.ResourceNotFoundException;
import com.jobtracker.demo.repository.JobApplicationRepository;
import com.jobtracker.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobRepo;
    private final UserRepository userRepo;
    private final S3Service s3Service;

    @Override
    public JobResponse create(JobRequest request, String email) {
        User user = getUser(email);
        JobApplication job = JobApplication.builder()
                .companyName(request.getCompanyName())
                .role(request.getRole())
                .status(request.getStatus())
                .appliedDate(request.getAppliedDate())
                .jobDescription(request.getJobDescription())
                .notes(request.getNotes())
                .user(user)
                .build();
        return toResponse(jobRepo.save(job));
    }

    @Override
    public Page<JobResponse> getAll(String email, JobStatus status, LocalDate from, LocalDate to, Pageable pageable) {
        Long userId = getUser(email).getId();

        if (status != null && from != null && to != null) {
            return jobRepo.findByUserIdAndStatusAndAppliedDateBetween(userId, status, from, to, pageable).map(this::toResponse);
        } else if (status != null) {
            return jobRepo.findByUserIdAndStatus(userId, status, pageable).map(this::toResponse);
        } else if (from != null && to != null) {
            return jobRepo.findByUserIdAndAppliedDateBetween(userId, from, to, pageable).map(this::toResponse);
        }
        return jobRepo.findByUserId(userId, pageable).map(this::toResponse);
    }

    @Override
    public JobResponse update(Long id, JobRequest request, String email) {
        JobApplication job = getJobForUser(id, email);
        job.setCompanyName(request.getCompanyName());
        job.setRole(request.getRole());
        job.setStatus(request.getStatus());
        job.setAppliedDate(request.getAppliedDate());
        job.setJobDescription(request.getJobDescription());
        job.setNotes(request.getNotes());
        return toResponse(jobRepo.save(job));
    }

    @Override
    public void delete(Long id, String email) {
        JobApplication job = getJobForUser(id, email);
        jobRepo.delete(job);
    }

    @Override
    public JobResponse uploadResume(Long id, MultipartFile file, String email) {
        JobApplication job = getJobForUser(id, email);
        String url = s3Service.uploadFile(file);
        job.setResumeUrl(url);
        return toResponse(jobRepo.save(job));
    }

    private User getUser(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private JobApplication getJobForUser(Long id, String email) {
        Long userId = getUser(email).getId();
        return jobRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));
    }

    private JobResponse toResponse(JobApplication job) {
        return JobResponse.builder()
                .id(job.getId())
                .companyName(job.getCompanyName())
                .role(job.getRole())
                .status(job.getStatus())
                .appliedDate(job.getAppliedDate())
                .jobDescription(job.getJobDescription())
                .resumeUrl(s3Service.getPresignedUrl(job.getResumeUrl()))
                .notes(job.getNotes())
                .build();
    }
}
