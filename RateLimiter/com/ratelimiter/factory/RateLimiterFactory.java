package com.ratelimiter.factory;

import com.ratelimiter.algorithms.FixedWindowRateLimiter;
import com.ratelimiter.algorithms.SlidingWindowCounterRateLimiter;
import com.ratelimiter.core.RateLimiter;
import com.ratelimiter.core.RateLimiterRule;

public class RateLimiterFactory {
    public enum Algorithm {
        FIXED_WINDOW, SLIDING_WINDOW_COUNTER
    }

    public static RateLimiter createRateLimiter(Algorithm type, RateLimiterRule rule) {
        switch (type) {
            case FIXED_WINDOW:
                return new FixedWindowRateLimiter(rule);
            case SLIDING_WINDOW_COUNTER:
                return new SlidingWindowCounterRateLimiter(rule);
            default:
                throw new IllegalArgumentException("Unknown rate limiter algorithm");
        }
    }
}