package com.jobtracker.demo.controller;

import com.jobtracker.demo.dto.JobRequest;
import com.jobtracker.demo.dto.JobResponse;
import com.jobtracker.demo.entity.JobStatus;
import com.jobtracker.demo.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobApplicationService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> create(@Valid @RequestBody JobRequest request, Authentication auth) {
        return ResponseEntity.ok(jobService.create(request, auth.getName()));
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> getAll(
            Authentication auth,
            @RequestParam(required = false) JobStatus status,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @PageableDefault(size = 10, sort = "appliedDate") Pageable pageable) {
        return ResponseEntity.ok(jobService.getAll(auth.getName(), status, from, to, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> update(@PathVariable Long id,
                                              @Valid @RequestBody JobRequest request,
                                              Authentication auth) {
        return ResponseEntity.ok(jobService.update(id, request, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        jobService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload-resume")
    public ResponseEntity<JobResponse> uploadResume(@PathVariable Long id,
                                                    @RequestParam("file") MultipartFile file,
                                                    Authentication auth) {
        return ResponseEntity.ok(jobService.uploadResume(id, file, auth.getName()));
    }
}
