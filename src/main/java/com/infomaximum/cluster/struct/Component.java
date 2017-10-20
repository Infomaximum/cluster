package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.manager.remote.managersubsystem.RControllerManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.active.ActiveComponentsImpl;
import com.infomaximum.cluster.core.remote.Remotes;
import com.infomaximum.cluster.core.service.transport.Transport;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.exception.ClusterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by kris on 15.06.17.
 */
public abstract class Component {

    private final static Logger log = LoggerFactory.getLogger(Component.class);

    private TransportManager transportManager;
    private String key;
    private Transport transport;
    private Remotes remote;
    private ActiveComponents subSystemHashActives;

    public final void init(TransportManager transportManager) throws ClusterException {
        this.transportManager = transportManager;
        this.key = generateKey();
        this.transport = transportManager.createTransport(getInfo().getUuid(), key);
        this.remote = new Remotes(this);

        try {
            transport.setExecutor(initExecutorTransport());
        } catch (ClusterException e) {
            transportManager.destroyTransport(transport);
            throw e;
        }

        //Регистрируемся у менеджера подсистем
        log.info("Register {} ver.{}...", getInfo().getUuid(), getInfo().getVersion());
        this.subSystemHashActives = registerComponent();

        //Загружаемся, в случае ошибки снимаем регистрацию
        try {
            load();
        } catch (ClusterException e) {
            log.error("{} Error init subsystem", this, e);
            unregisterComponent();
            transportManager.destroyTransport(transport);
            throw e;
        }
    }

    public abstract Info getInfo();
    public abstract void load() throws ClusterException;
    public abstract ExecutorTransport initExecutorTransport() throws ClusterException;
    public abstract void destroying() throws ClusterException;

    protected String generateKey() {
        return new StringBuilder()
                .append(getInfo().getUuid()).append(':').append(UUID.randomUUID().toString())
                .toString();
    }

    /**
     * Регистрируемся у менджера подсистем
     */
    protected ActiveComponentsImpl registerComponent() {
        RControllerManagerComponent rControllerManagerComponent = remote.getFromSSKey(ManagerComponent.KEY, RControllerManagerComponent.class);
        ComponentInfos activeComponents = rControllerManagerComponent.register(
                new RuntimeComponentInfo(key, getInfo().getUuid(), isSingleton(), getTransport().getExecutor().getClassRControllers())
        );
        return new ActiveComponentsImpl(this, activeComponents.getItems());
    }

    /**
     * Снимаем регистрацию у менджера подсистем
     */
    protected void unregisterComponent() {
        remote.getFromSSKey(ManagerComponent.KEY, RControllerManagerComponent.class).unregister(key);
    }

    public Transport getTransport(){
        return transport;
    }

    public String getKey() {
        return key;
    }

    public boolean isSingleton(){
        return true;
    }

    public Remotes getRemotes() {
        return remote;
    }

    public ActiveComponents getActiveRoles() {
        return subSystemHashActives;
    }

    public final void destroy(){
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
