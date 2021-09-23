package com.infomaximum.cluster;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.remote.packer.*;
import com.infomaximum.cluster.core.service.componentuuid.ComponentUuidManager;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.network.NetworkTransit;
import com.infomaximum.cluster.core.service.transport.network.local.LocalNetworkTransit;
import com.infomaximum.cluster.exception.ClusterDependencyException;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.exception.clusterDependencyCycleException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс потоконебезопасен
 */
public class Cluster implements AutoCloseable {

    private final static Logger log = LoggerFactory.getLogger(Cluster.class);

    public final byte node;

    private final TransportManager transportManager;
    private final ComponentUuidManager componentUuidManager;

    private final Map<Class<? extends Component>, List<Component>> components;
    private final List<Component> dependencyOrderedComponents;

    private final Object context;

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    private Cluster(TransportManager transportManager, Object context, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.node = transportManager.networkTransit.getNode();

        this.transportManager = transportManager;

        this.componentUuidManager = new ComponentUuidManager();

        this.components = new HashMap<>();
        this.dependencyOrderedComponents = new ArrayList<>();

        this.context = context;
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;

        log.info("Cluster created.");
    }

    public TransportManager getTransportManager() {
        return transportManager;
    }

    private void appendComponent(Component component) throws ClusterException {
        component.init(transportManager);

        List<Component> componentInstances = components.get(component.getClass());
        if (componentInstances == null) {
            componentInstances = new ArrayList<>();
            components.put(component.getClass(), componentInstances);
        }
        componentInstances.add(component);
        dependencyOrderedComponents.add(component);
    }

    public <T extends Component> T getAnyLocalComponent(Class<T> classComponent) {
        List<Component> components = this.components.get(classComponent);
        if (components == null) {
            return null;
        }
        return (T) components.get(RandomUtil.random.nextInt(components.size()));
    }

    //TODO Delete 01.01.2022

    /**
     * Use: getAnyLocalComponent
     *
     * @param classComponent
     * @param <T>
     * @return
     */
    @Deprecated
    public <T extends Component> T getAnyComponent(Class<T> classComponent) {
        return getAnyLocalComponent(classComponent);
    }

    //Не предпологаются частые вызовы - если будем дергать часто - необходимо переписать на итератор
    public Collection<Component> getLocalComponents() {
        List<Component> result = new ArrayList<>();
        for (Map.Entry<Class<? extends Component>, List<Component>> entry : this.components.entrySet()) {
            result.addAll(entry.getValue());
        }
        return result;
    }

    public String getUuid(Class<? extends Component> classComponent) {
        return componentUuidManager.getUuid(classComponent);
    }

    public <T> T getContext() {
        return (T) context;
    }

    public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return uncaughtExceptionHandler;
    }

    public Component getAnyLocalComponent(String uuidComponent) {
        for (Map.Entry<Class<? extends Component>, List<Component>> entry : this.components.entrySet()) {
            List<Component> components = entry.getValue();
            if (components.isEmpty()) continue;
            Component component = components.get(0);
            if (component.getInfo().getUuid().equals(uuidComponent)) return component;
        }
        return null;
    }

    public <T extends Component> List<T> getDependencyOrderedComponentsOf(Class<T> baseClass) {
        return dependencyOrderedComponents.stream()
                .filter(component -> baseClass.isAssignableFrom(component.getClass()))
                .map(component -> (T) component)
                .collect(Collectors.toList());
    }

    public void removeComponent(Component component) throws ClusterException {
        for (int i = dependencyOrderedComponents.size() - 1; i > -1; --i) {
            Component another = dependencyOrderedComponents.get(i);
            if (another == component) {
                dependencyOrderedComponents.remove(i);
                break;
            }

            if (Arrays.asList(another.getInfo().getDependencies()).contains(component.getClass())) {
                throw new ClusterDependencyException(another, component);
            }
        }

        closeComponent(component);
    }

    @Override
    public void close() {
        for (int i = dependencyOrderedComponents.size() - 1; i > -1; --i) {
            closeComponent(dependencyOrderedComponents.remove(i));
        }

        transportManager.destroy();
    }

    private void closeComponent(Component component) {
        List<Component> list = components.get(component.getClass());
        if (list != null) {
            list.remove(component);
            if (list.isEmpty()) {
                components.remove(component.getClass());
            }
        }

        component.destroy();
    }

    public static class Builder {

        private final List<RemotePacker> remotePackers;

        private NetworkTransit.Builder builderNetworkTransit;

        private List<ComponentBuilder> componentBuilders = new ArrayList<>();

        private Object context;

        private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

        public Builder() {
            this.remotePackers = new ArrayList<>();
            this.remotePackers.add(new RemotePackerRemoteObject());
            this.remotePackers.add(new RemotePackerSerializable());
            this.remotePackers.add(new RemotePackerFuture());
            this.remotePackers.add(new RemotePackerOptional());
            this.remotePackers.add(new RemotePackerClasterInputStream());

            this.builderNetworkTransit = new LocalNetworkTransit.Builder();
        }

        public Builder withNetworkTransport(NetworkTransit.Builder builderNetworkTransit) {
            this.builderNetworkTransit = builderNetworkTransit;
            return this;
        }

        public Builder withRemotePackerObject(RemotePacker remotePackerObject) {
            remotePackers.add(remotePackerObject);
            return this;
        }

        public Builder withComponent(ComponentBuilder componentBuilder) {
            if (containsComponent(componentBuilder)) {
                throw new RuntimeException(componentBuilder.getComponentClass() + " already exists.");
            }
            componentBuilders.add(componentBuilder);
            return this;
        }

        public Builder withComponentIfNotExist(ComponentBuilder componentBuilder) {
            if (!containsComponent(componentBuilder)) {
                componentBuilders.add(componentBuilder);
            }
            return this;
        }

        public Builder withContext(Object context) {
            this.context = context;
            return this;
        }

        public Builder withUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;
            return this;
        }

        public Cluster build() throws ClusterException {
            Cluster cluster = null;
            try {
                TransportManager transportManager = new TransportManager(builderNetworkTransit, remotePackers);

                cluster = new Cluster(transportManager, context, uncaughtExceptionHandler);

                List<Component> components = new ArrayList<>(componentBuilders.size() + 1);

                //TODO необходима правильная инициализация менеджера, в настоящий момент считаем, что приложение у нас одно поэтому инициализируем его прямо тут
                components.add(new ManagerComponent(cluster));
                for (ComponentBuilder builder : componentBuilders) {
                    components.add(builder.build(cluster));
                }
                appendNotExistenceDependencies(cluster, components);

                while (!components.isEmpty()) {
                    Component nextComponent = null;
                    int componentIndex = 0;
                    for (; componentIndex < components.size(); ++componentIndex) {
                        //Проверяем все ли зависимости загружены
                        Cluster finalCluster = cluster;
                        Component component = components.get(componentIndex);
                        boolean isSuccessDependencies = Arrays.stream(component.getInfo().getDependencies())
                                .noneMatch(aClass -> finalCluster.getAnyLocalComponent(aClass) == null);
                        if (isSuccessDependencies) {
                            nextComponent = component;
                            break;
                        }
                    }

                    if (nextComponent == null) {
                        throw new clusterDependencyCycleException(components.stream().map(cb -> cb.getClass().getName()).collect(Collectors.toList()));
                    }

                    cluster.appendComponent(nextComponent);

                    components.remove(componentIndex);
                }
            } catch (ClusterException ex) {
                if (cluster != null) {
                    cluster.close();
                }
                throw ex;
            }

            return cluster;
        }

        private static void appendNotExistenceDependencies(Cluster cluster, List<Component> source) throws ClusterException {
            Set<Class> componentClasses = source.stream().map(Component::getClass).collect(Collectors.toSet());
            for (int i = 0; i < source.size(); ++i) {
                for (Class dependence : source.get(i).getInfo().getDependencies()) {
                    if (!componentClasses.contains(dependence)) {
                        source.add(new ComponentBuilder(dependence).build(cluster));
                        componentClasses.add(dependence);
                    }
                }
            }
        }

        private boolean containsComponent(ComponentBuilder builder) {
            return componentBuilders.stream().anyMatch(cb -> cb.getComponentClass() == builder.getComponentClass());
        }
    }
}
