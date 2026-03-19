package com.jobtracker.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ReminderRequest {
    @NotNull
    private Long jobId;

    @NotNull
    private LocalDate reminderDate;

    private String message;
}
