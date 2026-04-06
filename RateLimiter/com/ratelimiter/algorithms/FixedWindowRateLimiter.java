package com.ratelimiter.algorithms;

import com.ratelimiter.core.RateLimiter;
import com.ratelimiter.core.RateLimiterRule;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FixedWindowRateLimiter implements RateLimiter {
    private final RateLimiterRule rule;
    private final ConcurrentMap<String, WindowData> store = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimiterRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean allowRequest(String key) {
        long currentTime = System.currentTimeMillis();
        long currentWindowStart = currentTime / rule.getWindowSizeInMillis();

        store.putIfAbsent(key, new WindowData(currentWindowStart));
        WindowData windowData = store.get(key);

        // Synchronize ONLY on the specific key's window object to avoid blocking other
        // keys
        synchronized (windowData) {
            if (windowData.windowStart != currentWindowStart) {
                // Time crossed into a new window, reset counter
                windowData.windowStart = currentWindowStart;
                windowData.count = 0;
            }

            if (windowData.count < rule.getMaxRequests()) {
                windowData.count++;
                return true;
            }
            return false;
        }
    }

    private static class WindowData {
        long windowStart;
        int count;

        WindowData(long windowStart) {
            this.windowStart = windowStart;
            this.count = 0;
        }
    }
}