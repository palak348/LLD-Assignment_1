package eviction;

import java.util.LinkedHashSet;

public class LRUEvictionPolicy<K> implements EvictionPolicy<K> {
    // LinkedHashSet maintains insertion order. We remove and re-add to keep recently used at the end.
    private final LinkedHashSet<K> accessOrder;

    public LRUEvictionPolicy() {
        this.accessOrder = new LinkedHashSet<>();
    }

    @Override
    public void keyAccessed(K key) {
        if (accessOrder.contains(key)) {
            accessOrder.remove(key);
        }
        accessOrder.add(key);
    }

    @Override
    public K evictKey() {
        if (accessOrder.isEmpty()) {
            return null;
        }
        // First element is the least recently used
        K firstKey = accessOrder.iterator().next();
        accessOrder.remove(firstKey);
        return firstKey;
    }
}