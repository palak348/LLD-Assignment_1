package com.ratelimiter.core;

public class RateLimiterRule {
    private final int maxRequests;
    private final long windowSizeInMillis;

    public RateLimiterRule(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public long getWindowSizeInMillis() {
        return windowSizeInMillis;
    }
}