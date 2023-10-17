package com.infomaximum.cluster.networktransit;

import com.infomaximum.cluster.Cluster;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpaceNetworkTransit {

    private final Map<UUID, Cluster> clusters;

    public SpaceNetworkTransit() {
        this.clusters = new HashMap<>();
    }

    public void registry(UUID nodeRuntimeId, Cluster cluster) {
        clusters.put(nodeRuntimeId, cluster);
    }

    public Cluster getCluster(UUID nodeRuntimeId) {
        Cluster cluster = clusters.get(nodeRuntimeId);
        if (cluster == null) {
            throw new RuntimeException("Cluster: " + nodeRuntimeId + " unknown");
        }
        return cluster;
    }

    public Collection<Cluster> getClusters() {
        return clusters.values();
    }
}
