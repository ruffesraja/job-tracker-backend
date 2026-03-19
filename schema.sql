-- Smart Job Application Tracker - Database Schema
-- Run this against your PostgreSQL database

CREATE DATABASE jobtracker;

\c jobtracker;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE job_applications (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('APPLIED', 'INTERVIEW', 'OFFER', 'REJECTED')),
    applied_date DATE NOT NULL,
    resume_url TEXT,
    notes TEXT,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE reminders (
    id BIGSERIAL PRIMARY KEY,
    reminder_date DATE NOT NULL,
    message TEXT,
    notified BOOLEAN NOT NULL DEFAULT FALSE,
    job_id BIGINT NOT NULL REFERENCES job_applications(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_job_user_id ON job_applications(user_id);
CREATE INDEX idx_job_status ON job_applications(status);
CREATE INDEX idx_job_applied_date ON job_applications(applied_date);
CREATE INDEX idx_reminder_date_notified ON reminders(reminder_date, notified);
