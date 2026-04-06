package core;

import eviction.EvictionPolicy;
import storage.Storage;

public class CacheNode<K, V> {
    private final String nodeId;
    private final int capacity;
    private final Storage<K, V> storage;
    private final EvictionPolicy<K> evictionPolicy;
    private int currentSize;

    public CacheNode(String nodeId, int capacity, Storage<K, V> storage, EvictionPolicy<K> evictionPolicy) {
        this.nodeId = nodeId;
        this.capacity = capacity;
        this.storage = storage;
        this.evictionPolicy = evictionPolicy;
        this.currentSize = 0;
    }

    public void put(K key, V value) {
        if (storage.contains(key)) {
            storage.add(key, value);
            evictionPolicy.keyAccessed(key);
            return;
        }
        if (currentSize >= capacity) {
            K keyToEvict = evictionPolicy.evictKey();
            if (keyToEvict != null) {
                storage.remove(keyToEvict);
                currentSize--;
                System.out.println("Evicted key: " + keyToEvict + " from node: " + nodeId);
            }
        }
        storage.add(key, value);
        evictionPolicy.keyAccessed(key);
        currentSize++;
    }

    public V get(K key) {
        if (storage.contains(key)) {
            evictionPolicy.keyAccessed(key);
            return storage.get(key);
        }
        return null;
    }
    
    public String getNodeId() { 
        return nodeId; 
    }
}