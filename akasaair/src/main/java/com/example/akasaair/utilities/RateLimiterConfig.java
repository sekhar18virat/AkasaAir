package com.example.akasaair.utilities;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.create(5.0);
    }

}

