package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.manager.remote.managersubsystem.RControllerManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.environment.EnvironmentComponents;
import com.infomaximum.cluster.core.remote.Remotes;
import com.infomaximum.cluster.core.service.transport.Transport;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.exception.ClusterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 15.06.17.
 */
public abstract class Component {

    private final static Logger log = LoggerFactory.getLogger(Component.class);

    protected final Cluster cluster;
    private TransportManager transportManager;
    private int uniqueId;
    private Transport transport;
    private Remotes remote;
    private ActiveComponents activeComponents;

    public Component(Cluster cluster) {
        this.cluster = cluster;
    }

    public void init(TransportManager transportManager) throws ClusterException {
        this.transportManager = transportManager;
        this.transport = transportManager.createTransport(this);
        this.remote = new Remotes(cluster, this);

        try {
            transport.setExecutor(initExecutorTransport());
        } catch (ClusterException e) {
            transportManager.destroyTransport(transport);
            throw e;
        }

        //Регистрируемся у менеджера подсистем
        this.activeComponents = registerComponent();
        log.info("Register {} ({})", getInfo().getUuid(), getUniqueId());
    }

    public abstract Info getInfo();

    public abstract ExecutorTransport initExecutorTransport() throws ClusterException;

    public abstract void destroying() throws ClusterException;

    /**
     * Регистрируемся у менджера подсистем
     */
    protected ActiveComponents registerComponent() {
        RControllerManagerComponent rControllerManagerComponent = remote.getFromCKey(ManagerComponent.MANAGER_UNIQUE_ID, RControllerManagerComponent.class);
        RegistrationState registrationState = rControllerManagerComponent.register(
                new RuntimeComponentInfo(getInfo(), isSingleton(), getTransport().getExecutor().getClassRControllers())
        );
        uniqueId = registrationState.uniqueId;
        transportManager.registerTransport(transport);
        return new ActiveComponents(this, registrationState.getItems());
    }

    /**
     * Снимаем регистрацию у менджера подсистем
     */
    protected void unregisterComponent() {
        remote.getFromCKey(ManagerComponent.MANAGER_UNIQUE_ID, RControllerManagerComponent.class).unregister(uniqueId);
    }

    public Transport getTransport() {
        return transport;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public boolean isSingleton() {
        return true;
    }

    public Remotes getRemotes() {
        return remote;
    }

    public EnvironmentComponents getEnvironmentComponents() {
        return activeComponents;
    }

    public final void destroy() {
        log.info("{} destroying...", getInfo().getUuid());
        try {
            destroying();
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
