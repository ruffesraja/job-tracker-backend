package com.jobtracker.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@Slf4j
public class AwsConfig {

    @Value("${aws.s3.region:us-east-1}")
    private String region;

    @Value("${aws.s3.access-key:#{null}}")
    private String accessKey;

    @Value("${aws.s3.secret-key:#{null}}")
    private String secretKey;

    @Value("${aws.s3.bucket-name:}")
    private String bucketName;

    @Bean
    public S3Client s3Client() {
        log.info("=== AWS Config ===");
        log.info("Region: {}", region);
        log.info("Bucket: {}", bucketName);
        log.info("Access Key provided: {}", accessKey != null && !accessKey.isBlank());
        if (accessKey != null && accessKey.length() > 8) {
            log.info("Access Key starts with: {}...", accessKey.substring(0, 8));
        }

        S3Client client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider())
                .build();

        try {
            client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            log.info("S3 bucket '{}' is accessible ✓", bucketName);
        } catch (Exception e) {
            log.error("S3 bucket '{}' is NOT accessible: {}", bucketName, e.getMessage());
        }

        return client;
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider())
                .build();
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider())
                .build();
    }

    private AwsCredentialsProvider credentialsProvider() {
        if (accessKey != null && !accessKey.isBlank()
                && secretKey != null && !secretKey.isBlank()) {
            log.info("Using static credentials");
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey));
        }
        log.info("Using default credentials provider chain");
        return DefaultCredentialsProvider.create();
    }
}
