package com.infomaximum.cluster.networktransit;

import com.infomaximum.cluster.Cluster;

import java.util.HashMap;
import java.util.Map;

public class SpaceNetworkTransit {

    private final Map<Integer, Cluster> clusters;

    public SpaceNetworkTransit() {
        this.clusters = new HashMap<>();
    }

    public void registry(int node, Cluster cluster) {
        clusters.put(node, cluster);
    }

    public Cluster getCluster(int node) {
        Cluster cluster = clusters.get(node);
        if (cluster == null) {
            throw new RuntimeException("Cluster: " + node + " unknown");
        }
        return cluster;
    }
}
