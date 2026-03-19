package com.jobtracker.demo.service;

public interface EmailService {
    void sendReminder(String to, String companyName, String role, String message);
}
