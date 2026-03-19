package com.jobtracker.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final SesClient sesClient;

    @Value("${aws.ses.from-email:}")
    private String fromEmail;

    @Override
    public void sendReminder(String to, String companyName, String role, String message) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(fromEmail)
                    .destination(Destination.builder().toAddresses(to).build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data("Job Tracker Reminder: " + companyName + " - " + role)
                                    .build())
                            .body(Body.builder()
                                    .text(Content.builder()
                                            .data("Hi,\n\nThis is a reminder for your job application:\n\n"
                                                    + "Company: " + companyName + "\n"
                                                    + "Role: " + role + "\n"
                                                    + "Note: " + message + "\n\n"
                                                    + "— Job Tracker")
                                            .build())
                                    .build())
                            .build())
                    .build();

            sesClient.sendEmail(request);
            log.info("Reminder email sent to {} for {}", to, companyName);
        } catch (SesException e) {
            log.error("Failed to send email to {}: {}", to, e.awsErrorDetails().errorMessage());
        }
    }
}
