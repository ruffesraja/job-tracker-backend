package com.jobtracker.demo.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class ReminderResponse {
    private Long id;
    private Long jobId;
    private String companyName;
    private LocalDate reminderDate;
    private String message;
    private boolean notified;
}
