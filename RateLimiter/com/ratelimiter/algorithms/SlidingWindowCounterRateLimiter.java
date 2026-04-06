package com.ratelimiter.algorithms;

import com.ratelimiter.core.RateLimiter;
import com.ratelimiter.core.RateLimiterRule;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SlidingWindowCounterRateLimiter implements RateLimiter {
    private final RateLimiterRule rule;
    private final ConcurrentMap<String, SlidingWindowData> store = new ConcurrentHashMap<>();

    public SlidingWindowCounterRateLimiter(RateLimiterRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean allowRequest(String key) {
        long currentTime = System.currentTimeMillis();
        long currentWindowKey = currentTime / rule.getWindowSizeInMillis();

        store.putIfAbsent(key, new SlidingWindowData(currentWindowKey));
        SlidingWindowData windowData = store.get(key);

        synchronized (windowData) {
            long prevWindowKey = currentWindowKey - 1;

            // If the stored window is out of date, shift windows
            if (windowData.currentWindowKey != currentWindowKey) {
                if (windowData.currentWindowKey == prevWindowKey) {
                    windowData.prevCount = windowData.currentCount;
                } else {
                    // We skipped a whole window entirely
                    windowData.prevCount = 0;
                }
                windowData.currentWindowKey = currentWindowKey;
                windowData.currentCount = 0;
            }

            // Calculate the overlap percentage of the previous window
            double overlapPercentage = (double) (rule.getWindowSizeInMillis()
                    - (currentTime % rule.getWindowSizeInMillis())) / rule.getWindowSizeInMillis();
            int estimatedCount = (int) (windowData.prevCount * overlapPercentage) + windowData.currentCount;

            if (estimatedCount < rule.getMaxRequests()) {
                windowData.currentCount++;
                return true;
            }
            return false;
        }
    }

    private static class SlidingWindowData {
        long currentWindowKey;
        int currentCount;
        int prevCount;

        SlidingWindowData(long currentWindowKey) {
            this.currentWindowKey = currentWindowKey;
            this.currentCount = 0;
            this.prevCount = 0;
        }
    }
}