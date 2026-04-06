package eviction;

public interface EvictionPolicy<K> {
    void keyAccessed(K key);
    K evictKey();
}