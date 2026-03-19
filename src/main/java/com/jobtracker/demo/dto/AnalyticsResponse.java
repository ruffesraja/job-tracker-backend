package com.jobtracker.demo.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class AnalyticsResponse {
    private long totalApplications;
    private Map<String, Long> countByStatus;
    private Map<String, Long> monthlyTrends; // "2026-03" -> count
}
