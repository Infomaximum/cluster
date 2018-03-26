package com.infomaximum.cluster.test;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.ComponentBuilder;
import com.infomaximum.cluster.builder.transport.MockTransportBuilder;
import com.infomaximum.cluster.server.custom.CustomComponent;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.exception.CyclicDependenceException;
import com.infomaximum.cluster.exception.DependencyException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ClusterTest {

    @Test
    public void createValidCluster() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withTransport(
                        new MockTransportBuilder()
                )
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .build()) {
        }
    }

    @Test
    public void implicitCreateComponent() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withTransport(
                        new MockTransportBuilder()
                )
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
                .withTransport(
                        new MockTransportBuilder()
                )
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
                .withTransport(
                        new MockTransportBuilder()
                )
                .withComponent(new ComponentBuilder(CyclicComponent1.class))
                .withComponent(new ComponentBuilder(CyclicComponent2.class))
                .build()) {
            Assert.fail();
        } catch (CyclicDependenceException ex) {
            Assert.assertTrue(true);
        }

        try (Cluster cluster = new Cluster.Builder()
                .withTransport(
                        new MockTransportBuilder()
                )
                .withComponent(new ComponentBuilder(CyclicComponent1.class))
                .build()) {
            Assert.fail();
        } catch (CyclicDependenceException ex) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void dependenceOrdered1() throws Exception {
        try (Cluster cluster = new Cluster.Builder()
                .withTransport(
                        new MockTransportBuilder()
                )
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
                .withTransport(
                        new MockTransportBuilder()
                )
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
                .withTransport(new MockTransportBuilder())
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .withComponent(new ComponentBuilder(Component2.class))
                .withComponent(new ComponentBuilder(Component1.class))
                .withComponent(new ComponentBuilder(Component3.class))
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .build()) {

            try {
                cluster.removeComponent(cluster.getAnyComponent(Component2.class));
                Assert.fail();
            } catch (DependencyException e) {
                Assert.assertTrue(true);
            }

            cluster.removeComponent(cluster.getAnyComponent(Component1.class));
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

        @Override
        public void load() throws ClusterException {
        }

        @Override
        public ExecutorTransportImpl initExecutorTransport() throws ClusterException {
            return new ExecutorTransportImpl.Builder(this).build();
        }

        @Override
        public void destroying() throws ClusterException {
        }
    }

    public static class Component1 extends BaseComponent {

        public static final Info INFO = new Info.Builder(Component1.class.getPackage().getName())
                .withComponentClass(Component1.class)
                .withDependence(Component2.class)
                .build();

        public Component1(Cluster cluster) {
            super(cluster);
        }

        @Override
        public Info getInfo() {
            return INFO;
        }
    }

    public static class Component2 extends BaseComponent {

        public static final Info INFO = new Info.Builder(Component2.class.getPackage().getName())
                .withComponentClass(Component2.class)
                .withDependence(Component3.class)
                .build();

        public Component2(Cluster cluster) {
            super(cluster);
        }

        @Override
        public Info getInfo() {
            return INFO;
        }
    }

    public static class Component3 extends BaseComponent {

        public static final Info INFO = new Info.Builder(Component3.class.getPackage().getName())
                .withComponentClass(Component3.class)
                .withDependence(MemoryComponent.class)
                .build();

        public Component3(Cluster cluster) {
            super(cluster);
        }

        @Override
        public Info getInfo() {
            return INFO;
        }
    }

    public static class CyclicComponent1 extends Component {

        public static final Info INFO = new Info.Builder(CyclicComponent1.class.getPackage().getName())
                .withComponentClass(CyclicComponent1.class)
                .withDependence(CustomComponent.class)
                .withDependence(CyclicComponent1.class)
                .build();

        public CyclicComponent1(Cluster cluster) {
            super(cluster);
        }

        @Override
        public void load() throws ClusterException {
        }

        @Override
        public ExecutorTransportImpl initExecutorTransport() throws ClusterException {
            return new ExecutorTransportImpl.Builder(this).build();
        }

        @Override
        public Info getInfo() {
            return INFO;
        }

        @Override
        public void destroying() throws ClusterException {
        }
    }

    public static class CyclicComponent2 extends Component {

        public static final Info INFO = new Info.Builder(CyclicComponent2.class.getPackage().getName())
                .withComponentClass(CyclicComponent2.class)
                .withDependence(CyclicComponent1.class)
                .build();

        public CyclicComponent2(Cluster cluster) {
            super(cluster);
        }

        @Override
        public void load() throws ClusterException {
        }

        @Override
        public ExecutorTransportImpl initExecutorTransport() throws ClusterException {
            return new ExecutorTransportImpl.Builder(this).build();
        }

        @Override
        public Info getInfo() {
            return INFO;
        }

        @Override
        public void destroying() throws ClusterException {
        }
    }
}