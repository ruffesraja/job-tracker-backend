package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.ReminderRequest;
import com.jobtracker.demo.dto.ReminderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReminderService {
    ReminderResponse create(ReminderRequest request, String email);
    Page<ReminderResponse> getAll(String email, Boolean notified, Pageable pageable);
    void sendTestReminder(String email);
}
