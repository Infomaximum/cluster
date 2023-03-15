package com.infomaximum.tests.items;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.ComponentBuilder;
import com.infomaximum.cluster.anotation.Info;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.exception.ClusterDependencyException;
import com.infomaximum.cluster.exception.clusterDependencyCycleException;
import com.infomaximum.cluster.struct.Component;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ClusterTest {

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
        }
    };

    @Test
    public void createValidCluster() throws Exception {
        try (Cluster cluster = new Cluster.Builder(uncaughtExceptionHandler)
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .withComponentIfNotExist(new ComponentBuilder(Custom1Component.class))
                .withComponentIfNotExist(new ComponentBuilder(Custom1Component.class))
                .build()) {
        }
    }

    @Test
    public void implicitCreateComponent() throws Exception {
        try (Cluster cluster = new Cluster.Builder(uncaughtExceptionHandler)
                .withComponent(new ComponentBuilder(Component3.class))
                .build()) {

            List<Component> components = cluster.getDependencyOrderedComponentsOf(Component.class);

            Assertions.assertEquals(ManagerComponent.class, components.remove(0).getClass());
            Assertions.assertEquals(MemoryComponent.class, components.remove(0).getClass());
            Assertions.assertEquals(Component3.class, components.remove(0).getClass());

            Assertions.assertEquals(0, components.size());
        }
    }

    @Test
    public void componentAlreadyExists() throws Exception {
        try (Cluster cluster = new Cluster.Builder(uncaughtExceptionHandler)
                .withComponent(new ComponentBuilder(MemoryComponent.class))
                .withComponent(new ComponentBuilder(Custom1Component.class))
                .withComponent(new ComponentBuilder(MemoryComponent.class))
                .build()) {

        } catch (RuntimeException ex) {
            if (ex.getMessage().contains(MemoryComponent.class.getName())) {
                Assertions.assertTrue(true);
                return;
            }
        }

        Assertions.fail();
    }

    @Test
    public void cyclicDependence() throws Exception {
        try (Cluster cluster = new Cluster.Builder(uncaughtExceptionHandler)
                .withComponent(new ComponentBuilder(CyclicComponent1.class))
                .withComponent(new ComponentBuilder(CyclicComponent2.class))
                .build()) {
            Assertions.fail();
        } catch (clusterDependencyCycleException ex) {
            Assertions.assertTrue(true);
        }

        try (Cluster cluster = new Cluster.Builder(uncaughtExceptionHandler)
                .withComponent(new ComponentBuilder(CyclicComponent1.class))
                .build()) {
            Assertions.fail();
        } catch (clusterDependencyCycleException ex) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void dependenceOrdered1() throws Exception {
        try (Cluster cluster = new Cluster.Builder(uncaughtExceptionHandler)
                .withComponentIfNotExist(new ComponentBuilder(Custom1Component.class))
                .withComponent(new ComponentBuilder(Component2.class))
                .withComponent(new ComponentBuilder(Component1.class))
                .withComponent(new ComponentBuilder(Component3.class))
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .build()) {

            List<Component> components = cluster.getDependencyOrderedComponentsOf(Component.class);

            Assertions.assertEquals(ManagerComponent.class, components.remove(0).getClass());
            Assertions.assertEquals(Custom1Component.class, components.remove(0).getClass());
            Assertions.assertEquals(MemoryComponent.class, components.remove(0).getClass());
            Assertions.assertEquals(Component3.class, components.remove(0).getClass());
            Assertions.assertEquals(Component2.class, components.remove(0).getClass());
            Assertions.assertEquals(Component1.class, components.remove(0).getClass());

            Assertions.assertEquals(0, components.size());
        }
    }

    @Test
    public void dependenceOrdered2() throws Exception {
        try (Cluster cluster = new Cluster.Builder(uncaughtExceptionHandler)
                .withComponentIfNotExist(new ComponentBuilder(Custom1Component.class))
                .withComponent(new ComponentBuilder(Component2.class))
                .withComponent(new ComponentBuilder(Component1.class))
                .withComponent(new ComponentBuilder(Component3.class))
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .build()) {

            List<BaseComponent> components = cluster.getDependencyOrderedComponentsOf(BaseComponent.class);

            Assertions.assertEquals(Component3.class, components.remove(0).getClass());
            Assertions.assertEquals(Component2.class, components.remove(0).getClass());
            Assertions.assertEquals(Component1.class, components.remove(0).getClass());

            Assertions.assertEquals(0, components.size());
        }
    }

    @Test
    public void removeComponent() throws Exception {
        try (Cluster cluster = new Cluster.Builder(uncaughtExceptionHandler)
                .withComponentIfNotExist(new ComponentBuilder(Custom1Component.class))
                .withComponent(new ComponentBuilder(Component2.class))
                .withComponent(new ComponentBuilder(Component1.class))
                .withComponent(new ComponentBuilder(Component3.class))
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .build()) {

            try {
                cluster.removeComponent(cluster.getAnyLocalComponent(Component2.class));
                Assertions.fail();
            } catch (ClusterDependencyException e) {
                Assertions.assertTrue(true);
            }

            cluster.removeComponent(cluster.getAnyLocalComponent(Component1.class));
            List<BaseComponent> components = cluster.getDependencyOrderedComponentsOf(BaseComponent.class);
            Assertions.assertEquals(Component3.class, components.remove(0).getClass());
            Assertions.assertEquals(Component2.class, components.remove(0).getClass());
            Assertions.assertEquals(0, components.size());
        }
    }

    public static abstract class BaseComponent extends Component {

        public BaseComponent(Cluster cluster) {
            super(cluster);
        }

    }

    @Info(
            uuid = "com.infomaximum.tests.items",
            dependencies = {Component2.class}
    )
    public static class Component1 extends BaseComponent {

        public Component1(Cluster cluster) {
            super(cluster);
        }

        @Override
        public boolean isSingleton() {
            return false;
        }
    }

    @Info(
            uuid = "com.infomaximum.tests.items",
            dependencies = {Component3.class}
    )
    public static class Component2 extends BaseComponent {

        public Component2(Cluster cluster) {
            super(cluster);
        }

        @Override
        public boolean isSingleton() {
            return false;
        }

    }

    @Info(
            uuid = "com.infomaximum.tests.items",
            dependencies = {MemoryComponent.class}
    )
    public static class Component3 extends BaseComponent {

        public Component3(Cluster cluster) {
            super(cluster);
        }

        @Override
        public boolean isSingleton() {
            return false;
        }

    }

    @Info(
            uuid = "com.infomaximum.tests.items",
            dependencies = {Custom1Component.class, CyclicComponent1.class}
    )
    public static class CyclicComponent1 extends Component {

        public CyclicComponent1(Cluster cluster) {
            super(cluster);
        }

    }

    @Info(
            uuid = "com.infomaximum.tests.items",
            dependencies = {CyclicComponent2.class, CyclicComponent1.class}
    )
    public static class CyclicComponent2 extends Component {

        public CyclicComponent2(Cluster cluster) {
            super(cluster);
        }

    }
}
