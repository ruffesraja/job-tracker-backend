package com.jobtracker.demo.repository;

import com.jobtracker.demo.entity.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByJobApplicationUserId(Long userId);

    Page<Reminder> findByJobApplicationUserId(Long userId, Pageable pageable);

    Page<Reminder> findByJobApplicationUserIdAndNotified(Long userId, boolean notified, Pageable pageable);

    List<Reminder> findByReminderDateLessThanEqualAndNotifiedFalse(LocalDate date);
}
