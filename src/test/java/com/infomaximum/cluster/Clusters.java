package com.infomaximum.cluster;

import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.component.service.ServiceComponent;
import com.infomaximum.cluster.networktransit.FakeNetworkTransit;
import com.infomaximum.cluster.networktransit.SpaceNetworkTransit;
import com.infomaximum.cluster.utils.ExecutorUtil;

public class Clusters implements AutoCloseable {

    private Cluster cluster1;
    private Cluster cluster2;

    public Clusters(NetworkTransit.Builder builderNetworkTransit1, NetworkTransit.Builder builderNetworkTransit2, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        ExecutorUtil.executors.execute(() -> {
            cluster1 = new Cluster.Builder(uncaughtExceptionHandler)
                    .withNetworkTransport(builderNetworkTransit1)
                    .withComponentIfNotExist(new ComponentBuilder(ServiceComponent.class))
                    .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                    .withComponentIfNotExist(new ComponentBuilder(Custom1Component.class))
                    .build();
        });

        ExecutorUtil.executors.execute(() -> {
            cluster2 = new Cluster.Builder(uncaughtExceptionHandler)
                    .withNetworkTransport(builderNetworkTransit2)
                    .withComponentIfNotExist(new ComponentBuilder(ServiceComponent.class))
                    .withComponentIfNotExist(new ComponentBuilder(Custom1Component.class))
                    .build();
        });

        //Ожидаем старта
        while (!(cluster1 != null && cluster2 != null)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    public Cluster getCluster1() {
        return cluster1;
    }

    public Cluster getCluster2() {
        return cluster2;
    }

    @Override
    public void close() {
        cluster2.close();
        cluster1.close();
    }

    public static class Builder {

        public enum Item {
            CLUSTER1, CLUSTER2;
        }

        private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

        private final FakeNetworkTransit.Builder builderNetworkTransit1;
        private final FakeNetworkTransit.Builder builderNetworkTransit2;

        public Builder() {
            this(
                    new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            e.printStackTrace();
                        }
                    }
            );
        }

        public Builder(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;

            SpaceNetworkTransit spaceNetworkTransit = new SpaceNetworkTransit();


            builderNetworkTransit1 = new FakeNetworkTransit.Builder(spaceNetworkTransit, uncaughtExceptionHandler);

            builderNetworkTransit2 = new FakeNetworkTransit.Builder(spaceNetworkTransit, uncaughtExceptionHandler);
        }

        public Clusters build() {
            return new Clusters(builderNetworkTransit1, builderNetworkTransit2, uncaughtExceptionHandler);
        }
    }

}

