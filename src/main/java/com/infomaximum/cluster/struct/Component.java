package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.manager.ManagerComponent;
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
    private RuntimeComponentInfo runtimeComponentInfo;

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
            log.error("Error set transport executor", e);
            try {
                transportManager.destroyTransport(transport);
            } catch (Exception ignore) {
            }
            throw e;
        }

        onInitialized();
    }

    public void onInitialized() {
    }

    /**
     * Запускает компонент в четыре шага:
     * <ol>
     *     <li>{@link #allocateId()} — выделяет уникальный {@code id} компонента,
     *         не публикуя его в локальном реестре;</li>
     *     <li>{@link #registerTransport()} — регистрирует {@code LocalTransport} в {@code TransportManager},
     *         после чего компонент физически готов обслуживать входящие RPC;</li>
     *     <li>{@link #registerComponent()} — публикует компонент в локальном реестре
     *         (с этого момента он попадает в snapshot, рассылаемый другим нодам);</li>
     *     <li>{@link #notifyRegistered()} — уведомляет локальных слушателей о появлении компонента.</li>
     * </ol>
     *
     * <p>Порядок 1→2→3 гарантирует, что любая нода, получившая компонент в snapshot, найдёт у него
     * зарегистрированный {@code LocalTransport} при входящем RPC.
     */
    public void start() {
        //Регистрируемся у менеджера подсистем
        log.info("Register {}", getInfo().getUuid());
        allocateId();
        registerTransport();
        registerComponent();
        notifyRegistered();
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

    /**
     * Шаг 1 в {@link #start()}: выделяет уникальный {@code id} и инициализирует
     * {@link RuntimeComponentInfo} компонента. После этого {@link #getId()} возвращает валидный
     * {@code id}, но компонент ещё не виден через {@code LocalManagerRuntimeComponent}.
     */
    protected void allocateId() {
        ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
        this.registrationState = managerComponent.getRegisterComponent().allocateId();
        this.runtimeComponentInfo = new RuntimeComponentInfo(
                registrationState.id,
                getInfo().getUuid(),
                getInfo().getVersion(),
                getTransport().getExecutor().getClassRControllers()
        );
    }

    /**
     * Шаг 3 в {@link #start()}: публикует компонент в локальном реестре.
     * Вызывается строго после {@link #registerTransport()}, чтобы snapshot
     * не содержал компонент без зарегистрированного {@code LocalTransport}.
     */
    protected void registerComponent() {
        ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
        managerComponent.getRegisterComponent().registerLocalComponent(getRuntimeComponentInfo());
    }

    protected void registerTransport() {
        transportManager.registerTransport(transport);
    }

    /**
     * Завершающий шаг {@link #start()}: уведомляет подписчиков о зарегистрированном компоненте.
     */
    protected void notifyRegistered() {
        ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
        managerComponent.getRegisterComponent().startLocalComponent(getId());
    }

    //Снимаем регистрацию у менджера подсистем
    protected void unregisterComponent() {
        ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
        managerComponent.getRegisterComponent().unRegisterLocalComponent(getRuntimeComponentInfo());
    }

    public LocalTransport getTransport() {
        return transport;
    }

    public int getId() {
        if (registrationState == null) {
            throw new IllegalStateException(
                    "Component (" + getInfo().getUuid() + ") is not registered yet.");
        }
        return registrationState.id;
    }

    public Remotes getRemotes() {
        return remote;
    }

    public RuntimeComponentInfo getRuntimeComponentInfo() {
        if (runtimeComponentInfo == null) {
            this.runtimeComponentInfo = new RuntimeComponentInfo(
                    getInfo().getUuid(),
                    getInfo().getVersion(),
                    getTransport().getExecutor().getClassRControllers()
            );
        }
        return runtimeComponentInfo;
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
