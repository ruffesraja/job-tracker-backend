package com.jobtracker.demo.controller;

import com.jobtracker.demo.dto.ReminderRequest;
import com.jobtracker.demo.dto.ReminderResponse;
import com.jobtracker.demo.scheduler.ReminderScheduler;
import com.jobtracker.demo.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;
    private final ReminderScheduler reminderScheduler;

    @PostMapping
    public ResponseEntity<ReminderResponse> create(@Valid @RequestBody ReminderRequest request,
                                                   Authentication auth) {
        return ResponseEntity.ok(reminderService.create(request, auth.getName()));
    }

    @GetMapping
    public ResponseEntity<Page<ReminderResponse>> getAll(
            @RequestParam(required = false) Boolean notified,
            @PageableDefault(size = 9, sort = "reminderDate") Pageable pageable,
            Authentication auth) {
        return ResponseEntity.ok(reminderService.getAll(auth.getName(), notified, pageable));
    }

    @PostMapping("/test-email")
    public ResponseEntity<Map<String, String>> sendTestEmail(Authentication auth) {
        reminderService.sendTestReminder(auth.getName());
        return ResponseEntity.ok(Map.of("message", "Test reminder email sent to " + auth.getName()));
    }

    @PostMapping("/trigger-scheduler")
    public ResponseEntity<Map<String, String>> triggerScheduler() {
        reminderScheduler.processReminders();
        return ResponseEntity.ok(Map.of("message", "Scheduler triggered manually"));
    }
}
