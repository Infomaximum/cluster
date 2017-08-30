package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.component.database.datasource.DataSourceComponent;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.manager.remote.managersubsystem.RControllerManagerRole;
import com.infomaximum.cluster.core.component.RuntimeRoleInfo;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.active.ActiveComponentsImpl;
import com.infomaximum.cluster.core.remote.Remotes;
import com.infomaximum.cluster.core.service.transport.Transport;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.struct.config.ComponentConfig;
import com.infomaximum.rocksdb.core.objectsource.DomainObjectSource;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    private DomainObjectSource domainObjectSource;

    public Component(TransportManager transportManager, ComponentConfig config) throws Exception {
        this.transportManager=transportManager;

        this.key = generateKey(config);
        this.config = config;
        this.transport = transportManager.createTransport(getInfo().getUuid(), key);;

        setExecutorTransport(transport);

        this.remote = new Remotes(this);

        //Регистрируемся у менеджера подсистем
        log.info("register subsystem {} v.{}...", getInfo().getUuid(), getInfo().getVersion());
        this.subSystemHashActives = registerSubSystem();
    }

    public void init() throws Exception {
        //Загружаемся, в случае ошибки снимаем регистрацию
        try {
            domainObjectSource = initDomainObjectSource();
            nativeInit();
        } catch (Throwable e) {
            log.error("{} Error init subsystem", this, e);
            try {
                unRegisterRole();
            } catch (Exception ex) {
                log.error("{} Exception unRegisterRole", this, e);
            }
            throw e;
        }
    }

    public abstract void nativeInit() throws Exception ;

    protected DomainObjectSource initDomainObjectSource() throws RocksDBException, IOException {
        return new DomainObjectSource(new DataSourceComponent(this));
    }

    public abstract ExecutorTransport initExecutorTransport() throws ReflectiveOperationException;

    protected String generateKey(ComponentConfig config) {
        return new StringBuilder()
                .append(getInfo().getUuid()).append(':').append(UUID.randomUUID().toString())
                .toString();
    }

    public final DomainObjectSource getDomainObjectSource(){
        return domainObjectSource;
    }

    /**
     * Регистрируемся у менджера подсистем
     */
    protected ActiveComponentsImpl registerSubSystem() throws Exception {
        RControllerManagerRole rControllerManagerRole = remote.getFromSSKey(ManagerComponent.KEY, RControllerManagerRole.class);
        ComponentInfos activeSubSystems = rControllerManagerRole.register(
                new RuntimeRoleInfo(key, getInfo().getUuid(), isSingleton(), getTransport().getExecutor().getClassRControllers())
        );
        return new ActiveComponentsImpl(this, activeSubSystems.getItems());
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
    protected void unRegisterRole() throws Exception {
        remote.getFromSSKey(ManagerComponent.KEY, RControllerManagerRole.class).unregister(key);
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


    public void destroy(){
        log.info("Destroy {}...", getInfo().getUuid());
        try {
            nativeDestroy();
            unRegisterRole();
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

    public abstract void nativeDestroy() throws Exception;
}
