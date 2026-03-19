package com.jobtracker.demo.scheduler;

import com.jobtracker.demo.entity.Reminder;
import com.jobtracker.demo.repository.ReminderRepository;
import com.jobtracker.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final ReminderRepository reminderRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void processReminders() {
        List<Reminder> dueReminders =
                reminderRepository.findByReminderDateLessThanEqualAndNotifiedFalse(LocalDate.now());

        log.info("Found {} due reminder(s)", dueReminders.size());

        for (Reminder reminder : dueReminders) {
            try {
                String company = reminder.getJobApplication().getCompanyName();
                String role = reminder.getJobApplication().getRole();
                String userEmail = reminder.getJobApplication().getUser().getEmail();

                log.info("REMINDER: {} at {} -> {}", company, role, userEmail);

                emailService.sendReminder(userEmail, company, role, reminder.getMessage());

                reminder.setNotified(true);
                reminderRepository.save(reminder);
            } catch (Exception e) {
                log.error("Failed to process reminder {}: {}", reminder.getId(), e.getMessage());
            }
        }
    }
}
