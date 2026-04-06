package distribution;

import core.CacheNode;
import java.util.List;

public class ModuloDistributionStrategy<K, V> implements DistributionStrategy<K, V> {
    @Override
    public CacheNode<K, V> getNode(K key, List<CacheNode<K, V>> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("Nodes list cannot be empty");
        }
        int hash = Math.abs(key.hashCode());
        int nodeIndex = hash % nodes.size();
        return nodes.get(nodeIndex);
    }
}