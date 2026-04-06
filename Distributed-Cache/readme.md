# Distributed Cache - LLD

## Overview
This project is a Java implementation of a Distributed Cache System designed as a Low-Level Design (LLD) assignment.

The cache supports the following operations:
- `get(key)`
- `put(key, value)`

The system distributes data across multiple cache nodes. Each node has limited capacity and uses the LRU (Least Recently Used) eviction policy. The design is extensible, so new distribution strategies and eviction policies can be added later without changing the core classes.

---

## Requirements Covered
This implementation satisfies the following requirements:

- Cache is distributed across multiple cache nodes.
- Number of cache nodes is configurable.
- On `get(key)`:
  - If key exists in cache, return the value.
  - If key does not exist in cache, fetch from database, store in cache, and return it.
- On `put(key, value)`:
  - Store the value in the correct cache node based on the distribution strategy.
- Pluggable distribution strategy is supported.
- Pluggable eviction policy is supported.
- Current implementation uses:
  - Modulo-based distribution
  - LRU eviction policy

---

## Assumptions
- Keys are unique.
- Database is represented using a mock in-memory implementation.
- Real network communication between cache nodes is not required.
- This is an in-memory LLD design exercise.

---

## Project Structure

```text
src/
├── core/
│   ├── CacheNode.java
│   └── DistributedCache.java
├── storage/
│   ├── Storage.java
│   └── HashMapStorage.java
├── eviction/
│   ├── EvictionPolicy.java
│   └── LRUEvictionPolicy.java
├── distribution/
│   ├── DistributionStrategy.java
│   └── ModuloDistributionStrategy.java
├── database/
│   ├── Database.java
│   └── MockDatabase.java
└── Main.java
```

---

## Class Responsibilities

### 1. DistributedCache
This is the main class that acts as the entry point for cache operations.

Responsibilities:
- Accepts `get` and `put` requests
- Uses distribution strategy to find the correct cache node
- Handles cache miss by reading from database
- Stores fetched data back into cache

### 2. CacheNode
Represents a single cache node.

Responsibilities:
- Stores key-value pairs
- Applies capacity limit
- Uses eviction policy when node becomes full

### 3. Storage
An interface for storage operations.

Methods:
- `add(key, value)`
- `remove(key)`
- `get(key)`
- `contains(key)`

Current implementation:
- `HashMapStorage`

### 4. EvictionPolicy
An interface for eviction behavior.

Methods:
- `keyAccessed(key)`
- `evictKey()`

Current implementation:
- `LRUEvictionPolicy`

### 5. DistributionStrategy
An interface to decide which node should store a given key.

Method:
- `getNode(key, nodes)`

Current implementation:
- `ModuloDistributionStrategy`

### 6. Database
An interface representing the backend database.

Methods:
- `get(key)`
- `put(key, value)`

Current implementation:
- `MockDatabase`

---

## Working of the System

### Data Distribution
When a key is inserted, the system uses the distribution strategy to decide which cache node should store that key.

Current formula:
```java
hash(key) % numberOfNodes
```

This ensures that each key is routed to one specific node.

### Cache Hit
When `get(key)` is called:
- The distribution strategy identifies the correct node.
- If the key exists in that node, value is returned directly.
- The key is marked as recently used.

### Cache Miss
When `get(key)` is called and the key is not present in the cache:
- The system fetches the value from the database.
- If found, the value is stored in the cache.
- Then the value is returned.

### Eviction
Each cache node has limited capacity.
When a node is full and a new key needs to be inserted:
- The LRU eviction policy selects the least recently used key.
- That key is removed from the node.
- The new key is inserted.

---

## SOLID Principles Used

### Single Responsibility Principle
Each class has one clear responsibility:
- `CacheNode` manages node-level caching
- `DistributedCache` manages overall cache flow
- `LRUEvictionPolicy` handles eviction logic
- `ModuloDistributionStrategy` handles node routing
- `HashMapStorage` handles data storage

### Open/Closed Principle
The design is open for extension but closed for modification.
For example:
- A new eviction policy like LFU can be added by implementing `EvictionPolicy`
- A new routing strategy like Consistent Hashing can be added by implementing `DistributionStrategy`

### Liskov Substitution Principle
Any implementation of `EvictionPolicy`, `Storage`, `Database`, or `DistributionStrategy` can replace the current one without affecting the main logic.

### Interface Segregation Principle
Interfaces are small and focused. Classes only depend on methods they actually need.

### Dependency Inversion Principle
Core classes depend on interfaces, not concrete implementations.
For example:
- `CacheNode` depends on `Storage` and `EvictionPolicy`
- `DistributedCache` depends on `Database` and `DistributionStrategy`

---

## Why This Design is Extensible
This design is flexible because important behaviors are abstracted using interfaces.

Future extensions can include:
- `ConsistentHashingStrategy`
- `LFUEvictionPolicy`
- `MRUEvictionPolicy`
- External database implementation
- Real distributed node communication

No major changes are needed in `DistributedCache` or `CacheNode` to support these additions.

---

## How to Compile and Run

Open terminal inside the `src` folder and run:

### Compile
```bash
javac core/*.java database/*.java distribution/*.java eviction/*.java storage/*.java Main.java
```

### Run
```bash
java Main
```

---

## Sample Flow

### Put Operation
```java
cache.put("user1", "Alice");
```

Steps:
1. Value is stored in database.
2. Distribution strategy selects the correct node.
3. Value is stored in that node.
4. If node is full, LRU eviction is applied.

### Get Operation
```java
cache.get("user1");
```

Steps:
1. Distribution strategy selects the correct node.
2. If key is present, return from cache.
3. If key is absent, fetch from database.
4. Store fetched value in cache.
5. Return value.

---

## Output Behavior
While running the program, the console shows:
- Which node stores a key
- Whether a request is a cache hit or cache miss
- Which key gets evicted when capacity is exceeded

This makes the working of the system easy to understand during testing.

---

## Possible Improvements
Some future enhancements are:
- Implement Consistent Hashing
- Implement LFU and MRU eviction
- Add thread safety for concurrent access
- Add unit tests
- Add a custom doubly linked list for LRU instead of using built-in collections
- Add configuration-driven node creation

---

## Conclusion
This project demonstrates a clean and extensible object-oriented design for a distributed cache system. It uses interfaces and composition to keep the system modular, testable, and easy to extend in future.