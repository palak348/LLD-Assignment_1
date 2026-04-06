package core;

import database.Database;
import distribution.DistributionStrategy;
import java.util.List;

public class DistributedCache<K, V> {
    private final List<CacheNode<K, V>> nodes;
    private final DistributionStrategy<K, V> distributionStrategy;
    private final Database<K, V> database;

    public DistributedCache(List<CacheNode<K, V>> nodes,
            DistributionStrategy<K, V> distributionStrategy,
            Database<K, V> database) {
        this.nodes = nodes;
        this.distributionStrategy = distributionStrategy;
        this.database = database;
    }

    public void put(K key, V value) {
        // Update database (Write-through or write-around)
        database.put(key, value);

        // Store in appropriate cache node
        CacheNode<K, V> node = distributionStrategy.getNode(key, nodes);
        node.put(key, value);
        System.out.println("PUT: Inserted key '" + key + "' into node '" + node.getNodeId() + "'");
    }

    public V get(K key) {
        CacheNode<K, V> node = distributionStrategy.getNode(key, nodes);
        V value = node.get(key);

        if (value != null) {
            System.out.println("GET: Cache HIT for key '" + key + "' at node '" + node.getNodeId() + "'");
            return value;
        }

        System.out.println("GET: Cache MISS for key '" + key + "'. Fetching from DB...");
        value = database.get(key);

        if (value != null) {
            node.put(key, value); // Populate cache after DB fetch
            System.out.println("GET: Fetched from DB and stored in node '" + node.getNodeId() + "'");
        } else {
            System.out.println("GET: Key '" + key + "' not found in DB.");
        }
        return value;
    }
}