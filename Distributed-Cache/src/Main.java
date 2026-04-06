import core.CacheNode;
import core.DistributedCache;
import database.Database;
import database.MockDatabase;
import distribution.DistributionStrategy;
import distribution.ModuloDistributionStrategy;
import eviction.LRUEvictionPolicy;
import storage.HashMapStorage;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Initializing Distributed Cache ===");

        // 1. Create 3 Cache Nodes (Capacity of 2 each to easily test eviction)
        List<CacheNode<String, String>> nodes = new ArrayList<>();
        nodes.add(new CacheNode<>("Node-A", 2, new HashMapStorage<>(), new LRUEvictionPolicy<>()));
        nodes.add(new CacheNode<>("Node-B", 2, new HashMapStorage<>(), new LRUEvictionPolicy<>()));
        nodes.add(new CacheNode<>("Node-C", 2, new HashMapStorage<>(), new LRUEvictionPolicy<>()));

        // 2. Setup Database and Strategy
        Database<String, String> db = new MockDatabase<>();
        DistributionStrategy<String, String> strategy = new ModuloDistributionStrategy<>();

        // 3. Initialize Distributed Cache
        DistributedCache<String, String> cache = new DistributedCache<>(nodes, strategy, db);

        // 4. Test Putting Data
        System.out.println("\n--- Adding Data ---");
        cache.put("user1", "Alice");
        cache.put("user2", "Bob");
        cache.put("user3", "Charlie");
        
        // 5. Test Getting Data (Cache Hits)
        System.out.println("\n--- Fetching Data ---");
        cache.get("user1"); // Should be a HIT
        cache.get("user2"); // Should be a HIT
        
        // 6. Test DB Fallback (Cache Miss)
        System.out.println("\n--- Fetching Data Not in Cache ---");
        // Manually put something in DB without putting in cache
        db.put("user4", "Diana"); 
        cache.get("user4"); // Should MISS, fetch from DB, and store in cache
        cache.get("user4"); // Now should be a HIT

        // 7. Test Eviction
        System.out.println("\n--- Testing Eviction (Node Capacity is 2) ---");
        // We need to add keys that hash to the same node to trigger eviction.
        // Let's brute force add a few keys to guarantee one of the nodes gets full.
        cache.put("keyA", "DataA");
        cache.put("keyB", "DataB");
        cache.put("keyC", "DataC");
        cache.put("keyD", "DataD");
        cache.put("keyE", "DataE");
        cache.put("keyF", "DataF");
    }
}