package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.AnalyticsResponse;
import com.jobtracker.demo.entity.JobStatus;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.exception.ResourceNotFoundException;
import com.jobtracker.demo.repository.JobApplicationRepository;
import com.jobtracker.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final JobApplicationRepository jobRepo;
    private final UserRepository userRepo;

    @Override
    public AnalyticsResponse getAnalytics(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Long userId = user.getId();

        // Count by status
        Map<String, Long> countByStatus = new LinkedHashMap<>();
        for (JobStatus status : JobStatus.values()) {
            countByStatus.put(status.name(), jobRepo.countByUserIdAndStatus(userId, status));
        }

        // Monthly trends
        Map<String, Long> monthlyTrends = new LinkedHashMap<>();
        for (Object[] row : jobRepo.getMonthlyTrends(userId)) {
            monthlyTrends.put((String) row[0], (Long) row[1]);
        }

        return AnalyticsResponse.builder()
                .totalApplications(jobRepo.countByUserId(userId))
                .countByStatus(countByStatus)
                .monthlyTrends(monthlyTrends)
                .build();
    }
}
