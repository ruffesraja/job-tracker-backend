package com.jobtracker.demo.repository;

import com.jobtracker.demo.entity.JobApplication;
import com.jobtracker.demo.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Page<JobApplication> findByUserId(Long userId, Pageable pageable);

    Page<JobApplication> findByUserIdAndStatus(Long userId, JobStatus status, Pageable pageable);

    Page<JobApplication> findByUserIdAndAppliedDateBetween(Long userId, LocalDate from, LocalDate to, Pageable pageable);

    Page<JobApplication> findByUserIdAndStatusAndAppliedDateBetween(
            Long userId, JobStatus status, LocalDate from, LocalDate to, Pageable pageable);

    Optional<JobApplication> findByIdAndUserId(Long id, Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, JobStatus status);

    @Query("SELECT FUNCTION('TO_CHAR', j.appliedDate, 'YYYY-MM') as month, COUNT(j) " +
           "FROM JobApplication j WHERE j.user.id = :userId " +
           "GROUP BY FUNCTION('TO_CHAR', j.appliedDate, 'YYYY-MM') " +
           "ORDER BY month DESC")
    java.util.List<Object[]> getMonthlyTrends(@Param("userId") Long userId);
}
