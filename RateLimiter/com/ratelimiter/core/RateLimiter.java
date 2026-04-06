package com.ratelimiter.core;

public interface RateLimiter {
    /**
     * Evaluates if the external call is allowed based on the rate limit.
     * 
     * @param key The key to rate limit on (e.g., API key, tenant ID, user ID).
     * @return true if allowed, false if rejected.
     */
    boolean allowRequest(String key);
}