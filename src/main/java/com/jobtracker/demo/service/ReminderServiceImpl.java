package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.ReminderRequest;
import com.jobtracker.demo.dto.ReminderResponse;
import com.jobtracker.demo.entity.JobApplication;
import com.jobtracker.demo.entity.Reminder;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.exception.ResourceNotFoundException;
import com.jobtracker.demo.repository.JobApplicationRepository;
import com.jobtracker.demo.repository.ReminderRepository;
import com.jobtracker.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepo;
    private final JobApplicationRepository jobRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    @Override
    public ReminderResponse create(ReminderRequest request, String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        JobApplication job = jobRepo.findByIdAndUserId(request.getJobId(), user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));

        Reminder reminder = Reminder.builder()
                .reminderDate(request.getReminderDate())
                .message(request.getMessage())
                .jobApplication(job)
                .build();
        return toResponse(reminderRepo.save(reminder));
    }

    @Override
    public Page<ReminderResponse> getAll(String email, Boolean notified, Pageable pageable) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (notified != null) {
            return reminderRepo.findByJobApplicationUserIdAndNotified(user.getId(), notified, pageable)
                    .map(this::toResponse);
        }
        return reminderRepo.findByJobApplicationUserId(user.getId(), pageable).map(this::toResponse);
    }

    @Override
    public void sendTestReminder(String email) {
        emailService.sendReminder(email, "Sample Company", "Software Engineer",
                "This is a test reminder from Job Tracker!");
    }

    private ReminderResponse toResponse(Reminder r) {
        return ReminderResponse.builder()
                .id(r.getId())
                .jobId(r.getJobApplication().getId())
                .companyName(r.getJobApplication().getCompanyName())
                .reminderDate(r.getReminderDate())
                .message(r.getMessage())
                .notified(r.isNotified())
                .build();
    }
}
