package com.example.metrics;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Global metrics registry (Proper Thread-Safe Singleton).
 */
public class MetricsRegistry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static volatile MetricsRegistry INSTANCE;
    private static boolean initialized = false;

    private final Map<String, Long> counters = new HashMap<>();

    private MetricsRegistry() {
        synchronized (MetricsRegistry.class) {
            if (initialized) {
                throw new IllegalStateException("Singleton already initialized. Use getInstance().");
            }
            initialized = true;
        }
    }

    public static MetricsRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (MetricsRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MetricsRegistry();
                }
            }
        }
        return INSTANCE;
    }

    @Serial
    protected Object readResolve() {
        return getInstance();
    }

    public synchronized void setCount(String key, long value) {
        counters.put(key, value);
    }

    public synchronized void increment(String key) {
        counters.put(key, getCount(key) + 1);
    }

    public synchronized long getCount(String key) {
        return counters.getOrDefault(key, 0L);
    }

    public synchronized Map<String, Long> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }
}
