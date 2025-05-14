package com.example.demo.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public Bucket createNewBucket() {
        // Giới hạn 5 request trong 1 phút
        Bandwidth limit = Bandwidth.simple(5, Duration.ofMinutes(1));
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
} 