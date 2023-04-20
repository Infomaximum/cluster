package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.manager.remote.managersubsystem.RControllerManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.Remotes;
import com.infomaximum.cluster.core.service.transport.LocalTransport;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ComponentExecutorTransportImpl;
import com.infomaximum.cluster.exception.ClusterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 15.06.17.
 */
public abstract class Component {

    private final static Logger log = LoggerFactory.getLogger(Component.class);

    private final Info info;
    private Cluster cluster;
    private TransportManager transportManager;
    private RegistrationState registrationState;
    private LocalTransport transport;
    private Remotes remote;

    public Component() {
        this.info = createInfoBuilder().build();
    }

    public void init(Cluster cluster, TransportManager transportManager) {
        this.cluster = cluster;
        this.transportManager = transportManager;
        this.transport = transportManager.createTransport(this);
        this.remote = new Remotes(cluster, this);

        try {
            transport.setExecutor(getExecutorTransportBuilder().build());
        } catch (ClusterException e) {
            transportManager.destroyTransport(transport);
            throw e;
        }

        //Регистрируемся у менеджера подсистем
        registerComponent();
        log.info("Register {} ({})", getInfo().getUuid(), getUniqueId());
    }

    protected Cluster getCluster() {
        return cluster;
    }

    //Точка переопределения билдера info
    protected Info.Builder createInfoBuilder() {
        return new Info.Builder(this.getClass());
    }

    public Info getInfo() {
        return info;
    }

    protected ComponentExecutorTransportImpl.Builder getExecutorTransportBuilder() {
        return new ComponentExecutorTransportImpl.Builder(this, cluster.getUncaughtExceptionHandler());
    }

    //Регистрируемся у менджера подсистем
    protected void registerComponent() {
        RControllerManagerComponent rControllerManagerComponent = remote.getFromCKey(
                ManagerComponent.getComponentUniqueIdManager(cluster.node),
                RControllerManagerComponent.class
        );
        this.registrationState = rControllerManagerComponent.register(
                new RuntimeComponentInfo(
                        cluster.node,
                        getInfo().getUuid(), isSingleton(),
                        getTransport().getExecutor().getClassRControllers()
                )
        );
        transportManager.registerTransport(transport);
    }

    //Снимаем регистрацию у менджера подсистем
    protected void unregisterComponent() {
        remote.getFromCKey(ManagerComponent.getComponentUniqueIdManager(cluster.node), RControllerManagerComponent.class).unregister(getUniqueId());
    }

    public LocalTransport getTransport() {
        return transport;
    }

    public int getUniqueId() {
        return registrationState.uniqueId;
    }

    public boolean isSingleton() {
        return true;
    }

    public Remotes getRemotes() {
        return remote;
    }

    public void destroy() {
        log.info("{} destroy...", getInfo().getUuid());
        try {
            unregisterComponent();
            log.info("{} destroyed. completed", getInfo().getUuid());
        } catch (Exception e) {
            log.error("{} Error destroy subsystem", getInfo().getUuid(), e);
        }

        try {
            transportManager.destroyTransport(transport);
        } catch (Exception e) {
            log.error("{} Error transport destroy", getInfo().getUuid(), e);
        }
    }
}
