package com.jobtracker.demo.dto;

import com.jobtracker.demo.entity.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class JobRequest {
    @NotBlank
    private String companyName;

    @NotBlank
    private String role;

    @NotNull
    private JobStatus status;

    @NotNull
    private LocalDate appliedDate;

    private String jobDescription;

    private String notes;
}
