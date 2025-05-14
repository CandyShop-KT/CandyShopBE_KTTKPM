package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;

@Configuration
@EnableRetry
public class RetryConfig {
    // Cấu hình retry sẽ được áp dụng thông qua annotation @Retryable
    // Ví dụ: @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
} 