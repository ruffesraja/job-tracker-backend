package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.AnalyticsResponse;

public interface AnalyticsService {
    AnalyticsResponse getAnalytics(String email);
}
