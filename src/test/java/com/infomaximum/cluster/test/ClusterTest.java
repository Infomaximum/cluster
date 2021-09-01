package com.infomaximum.cluster.test;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.ComponentBuilder;
import com.infomaximum.cluster.anotation.Info;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.exception.ClusterDependencyException;
import com.infomaximum.cluster.exception.clusterDependencyCycleException;
import com.infomaximum.cluster.server.custom.CustomComponent;
import com.infomaximum.cluster.struct.Component;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ClusterTest {

    @Test
    public void createValidCluster() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .build()) {
        }
    }

    @Test
    public void implicitCreateComponent() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withComponent(new ComponentBuilder(Component3.class))
                .build()) {

            List<Component> components = cluster.getDependencyOrderedComponentsOf(Component.class);

            Assert.assertEquals(ManagerComponent.class, components.remove(0).getClass());
            Assert.assertEquals(MemoryComponent.class, components.remove(0).getClass());
            Assert.assertEquals(Component3.class, components.remove(0).getClass());

            Assert.assertEquals(0, components.size());
        }
    }

    @Test
    public void componentAlreadyExists() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withComponent(new ComponentBuilder(MemoryComponent.class))
                .withComponent(new ComponentBuilder(CustomComponent.class))
                .withComponent(new ComponentBuilder(MemoryComponent.class))
                .build()) {

        } catch (RuntimeException ex) {
            if (ex.getMessage().contains(MemoryComponent.class.getName())) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.fail();
    }

    @Test
    public void cyclicDependence() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withComponent(new ComponentBuilder(CyclicComponent1.class))
                .withComponent(new ComponentBuilder(CyclicComponent2.class))
                .build()) {
            Assert.fail();
        } catch (clusterDependencyCycleException ex) {
            Assert.assertTrue(true);
        }

        try (Cluster cluster = new Cluster.Builder()
                .withComponent(new ComponentBuilder(CyclicComponent1.class))
                .build()) {
            Assert.fail();
        } catch (clusterDependencyCycleException ex) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void dependenceOrdered1() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .withComponent(new ComponentBuilder(Component2.class))
                .withComponent(new ComponentBuilder(Component1.class))
                .withComponent(new ComponentBuilder(Component3.class))
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .build()) {

            List<Component> components = cluster.getDependencyOrderedComponentsOf(Component.class);

            Assert.assertEquals(ManagerComponent.class, components.remove(0).getClass());
            Assert.assertEquals(CustomComponent.class, components.remove(0).getClass());
            Assert.assertEquals(MemoryComponent.class, components.remove(0).getClass());
            Assert.assertEquals(Component3.class, components.remove(0).getClass());
            Assert.assertEquals(Component2.class, components.remove(0).getClass());
            Assert.assertEquals(Component1.class, components.remove(0).getClass());

            Assert.assertEquals(0, components.size());
        }
    }

    @Test
    public void dependenceOrdered2() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .withComponent(new ComponentBuilder(Component2.class))
                .withComponent(new ComponentBuilder(Component1.class))
                .withComponent(new ComponentBuilder(Component3.class))
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .build()) {

            List<BaseComponent> components = cluster.getDependencyOrderedComponentsOf(BaseComponent.class);

            Assert.assertEquals(Component3.class, components.remove(0).getClass());
            Assert.assertEquals(Component2.class, components.remove(0).getClass());
            Assert.assertEquals(Component1.class, components.remove(0).getClass());

            Assert.assertEquals(0, components.size());
        }
    }

    @Test
    public void removeComponent() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .withComponent(new ComponentBuilder(Component2.class))
                .withComponent(new ComponentBuilder(Component1.class))
                .withComponent(new ComponentBuilder(Component3.class))
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .build()) {

            try {
                cluster.removeComponent(cluster.getAnyLocalComponent(Component2.class));
                Assert.fail();
            } catch (ClusterDependencyException e) {
                Assert.assertTrue(true);
            }

            cluster.removeComponent(cluster.getAnyLocalComponent(Component1.class));
            List<BaseComponent> components = cluster.getDependencyOrderedComponentsOf(BaseComponent.class);
            Assert.assertEquals(Component3.class, components.remove(0).getClass());
            Assert.assertEquals(Component2.class, components.remove(0).getClass());
            Assert.assertEquals(0, components.size());
        }
    }

    public static abstract class BaseComponent extends Component {

        public BaseComponent(Cluster cluster) {
            super(cluster);
        }

    }

    @Info(
            uuid = "com.infomaximum.cluster.test",
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
            uuid = "com.infomaximum.cluster.test",
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
            uuid = "com.infomaximum.cluster.test",
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
            uuid = "com.infomaximum.cluster.test",
            dependencies = {CustomComponent.class, CyclicComponent1.class}
    )
    public static class CyclicComponent1 extends Component {

        public CyclicComponent1(Cluster cluster) {
            super(cluster);
        }

    }

    @Info(
            uuid = "com.infomaximum.cluster.test",
            dependencies = {CyclicComponent2.class, CyclicComponent1.class}
    )
    public static class CyclicComponent2 extends Component {

        public CyclicComponent2(Cluster cluster) {
            super(cluster);
        }

    }
}
