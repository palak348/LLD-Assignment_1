package database;

import java.util.HashMap;
import java.util.Map;

public class MockDatabase<K, V> implements Database<K, V> {
    private final Map<K, V> db = new HashMap<>();

    @Override
    public V get(K key) {
        return db.get(key);
    }

    @Override
    public void put(K key, V value) {
        db.put(key, value);
    }
}