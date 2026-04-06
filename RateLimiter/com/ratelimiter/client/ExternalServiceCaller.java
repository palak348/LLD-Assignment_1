package com.ratelimiter.client;

import com.ratelimiter.core.RateLimiter;
import com.ratelimiter.core.RateLimiterRule;
import com.ratelimiter.factory.RateLimiterFactory;

public class ExternalServiceCaller {
    private final RateLimiter rateLimiter;

    public ExternalServiceCaller(RateLimiterFactory.Algorithm algorithm, int maxRequests, long windowMs) {
        // Easily switchable limiters via the factory
        RateLimiterRule rule = new RateLimiterRule(maxRequests, windowMs);
        this.rateLimiter = RateLimiterFactory.createRateLimiter(algorithm, rule);
    }

    public void processClientRequest(String customerId, boolean requiresExternalCall) {
        System.out.println("Processing business logic for " + customerId + "...");

        // Only evaluate rate limits if the external call is actually required
        if (requiresExternalCall) {
            if (rateLimiter.allowRequest(customerId)) {
                System.out.println("External call APPROVED for " + customerId);
                // Call external API here...
            } else {
                System.out.println("External call REJECTED for " + customerId + " - Rate Limit Exceeded");
            }
        } else {
            System.out.println("No external call required for " + customerId);
        }
    }
}