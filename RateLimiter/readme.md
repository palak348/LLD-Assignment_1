# Pluggable Rate Limiting System

This project is a Low-Level Design (LLD) implementation of a Pluggable Rate Limiting System in Java. It is designed to control the usage of paid external resources by internal services, ensuring quotas are respected before an external API call is made.

## 🎯 Problem Statement
The goal is to design a rate-limiting module that:
1. Determines if a specific external call is allowed or denied based on varying keys (e.g., API key, user ID).
2. Supports multiple rate-limiting algorithms, specifically **Fixed Window Counter** and **Sliding Window Counter**.
3. Allows internal services to plug in and switch algorithms easily without changing their business logic.
4. Ensures thread safety, high performance, and adherence to SOLID principles.

---

## 🏛️ Architecture & Design Patterns

The system is highly modular, separating the configuration, mathematical algorithms, and the factory instantiation process. 

### 1. Strategy Pattern
The `RateLimiter` interface acts as the Strategy. Internal services (`ExternalServiceCaller`) only interact with this interface. This allows us to swap between `FixedWindowRateLimiter` and `SlidingWindowCounterRateLimiter` at runtime without altering the caller's code.

### 2. Factory Pattern
The `RateLimiterFactory` encapsulates the creation logic. By passing an `Algorithm` enum and a `RateLimiterRule`, the client gets a fully configured rate limiter. If a new algorithm (like Token Bucket) is added in the future, only the Factory needs to be updated.

---

## 🏗️ SOLID Principles Applied

- **Single Responsibility Principle (SRP):** 
  - `RateLimiterRule` only holds configuration data.
  - Algorithm classes only handle rate-limiting math.
  - `ExternalServiceCaller` only handles business flow.
- **Open/Closed Principle (OCP):** New algorithms (e.g., Leaky Bucket) can be added by implementing the `RateLimiter` interface without modifying existing code.
- **Liskov Substitution Principle (LSP):** Both algorithms implement the `RateLimiter` interface and can be used interchangeably by the client without breaking the system.
- **Interface Segregation Principle (ISP):** The `RateLimiter` interface exposes only a single, necessary method: `boolean allowRequest(String key)`.
- **Dependency Inversion Principle (DIP):** The `ExternalServiceCaller` depends on the abstraction (`RateLimiter` interface) rather than concrete implementations.

---

## ⚖️ Algorithm Trade-offs

| Feature | Fixed Window Counter | Sliding Window Counter |
| :--- | :--- | :--- |
| **Concept** | Resets the count entirely at the start of every new window. | Uses a weighted probability overlap of the previous and current window. |
| **Accuracy** | Prone to "Boundary Spikes" (a user can double their limit by requesting right before and right after a window resets). | Much smoother traffic handling. Eliminates boundary spikes by estimating the rolling rate. |
| **Memory** | Extremely low. Requires storing only one timestamp and one counter per user. | Low. Requires storing one timestamp and two counters (current and previous) per user. |
| **Speed** | Extremely fast, `O(1)` time complexity. | Slightly slower due to overlap fraction calculations, but still strictly `O(1)`. |

---

## ⚙️ Thread Safety
The application guarantees thread safety in a highly concurrent environment by utilizing `ConcurrentHashMap` for storing client data, combined with **fine-grained locking**. Instead of synchronizing the entire method (which would block all users), we synchronize only on the specific `WindowData` object for that user:
```java
synchronized (windowData) {
    // algorithm logic
}
```
This ensures `O(1)` performance and prevents race conditions for the same API key without slowing down requests from other keys.

---

## 🚀 How to Run the Project

### Prerequisites
- Java 8 or higher installed.

### Compilation
Navigate to the root directory (`RateLimiter/`) in your terminal and compile all the files:
```bash
javac com/ratelimiter/core/*.java com/ratelimiter/algorithms/*.java com/ratelimiter/factory/*.java com/ratelimiter/client/*.java Main.java
```

### Execution
Run the Main driver class to see the simulation of both algorithms:
```bash
java Main
```

*(Note: The Main class simulates rapid requests to demonstrate the system approving requests under the limit and successfully rejecting them once the limit is exceeded).*