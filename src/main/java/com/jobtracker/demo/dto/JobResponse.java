package com.jobtracker.demo.dto;

import com.jobtracker.demo.entity.JobStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class JobResponse {
    private Long id;
    private String companyName;
    private String role;
    private JobStatus status;
    private LocalDate appliedDate;
    private String jobDescription;
    private String resumeUrl;
    private String notes;
}
