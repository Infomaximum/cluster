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
import com.infomaximum.cluster.struct.config.ComponentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by kris on 15.06.17.
 */
public abstract class Component {

    private final static Logger log = LoggerFactory.getLogger(Component.class);

    private final TransportManager transportManager;

    private final String key;
    protected final ComponentConfig config;

    private final Transport transport;

    private final Remotes remote;

    private final ActiveComponents subSystemHashActives;

    public Component(TransportManager transportManager, ComponentConfig config) throws ReflectiveOperationException {
        this.transportManager=transportManager;

        this.key = generateKey(config);
        this.config = config;
        this.transport = transportManager.createTransport(getInfo().getUuid(), key);;

        this.remote = new Remotes(this);

        setExecutorTransport(transport);

        //Регистрируемся у менеджера подсистем
        log.info("register subsystem {} v.{}...", getInfo().getUuid(), getInfo().getVersion());
        this.subSystemHashActives = registerComponent();
    }

    public void init() {
        //Загружаемся, в случае ошибки снимаем регистрацию
        try {
            load();
        } catch (Throwable e) {
            log.error("{} Error init subsystem", this, e);
            try {
                unRegisterComponent();
            } catch (Exception ex) {
                log.error("{} Exception unRegisterComponent", this, e);
            }
            throw new RuntimeException(e);
        }
    }

    public abstract void load() throws Exception ;


    public abstract ExecutorTransport initExecutorTransport() throws ReflectiveOperationException;

    protected String generateKey(ComponentConfig config) {
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

    public Transport getTransport(){
        return transport;
    }

    protected void setExecutorTransport(Transport transport) throws ReflectiveOperationException {
        transport.setExecutor(initExecutorTransport());
    }

    /**
     * Снимаем регистрацию у менджера подсистем
     */
    protected void unRegisterComponent() throws Exception {
        remote.getFromSSKey(ManagerComponent.KEY, RControllerManagerComponent.class).unregister(key);
    }

    public abstract Info getInfo();

    public String getKey() {
        return key;
    }
    public ComponentConfig getConfig() {
        return config;
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
        log.info("Destroy {}...", getInfo().getUuid());
        try {
            destroyed();
            unRegisterComponent();
            log.info("Destroy {}... completed", getInfo().getUuid());
        } catch (Exception e) {
            log.error("{} Error destroy subsystem", getInfo().getUuid(), e);
        }

        try {
            transportManager.destroyTransport(transport);
        } catch (Exception e) {
            log.error("{} Error transport destroy subsystem", getInfo().getUuid(), e);
        }
    }

    public abstract void destroyed() throws Exception;
}
