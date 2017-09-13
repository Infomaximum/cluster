package com.infomaximum.cluster.builder;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.builder.component.ComponentBuilder;
import com.infomaximum.cluster.builder.transport.TransportBuilder;
import com.infomaximum.cluster.core.service.loader.LoaderComponents;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.struct.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by kris on 15.06.17.
 */
public class ClusterBuilder {

    private TransportBuilder transportBuilder;

    private Set<ComponentBuilder> componentBuilders;
    private String coreBuildVersion;

    public ClusterBuilder() {}

    public ClusterBuilder withCoreBuildVersion(String coreBuildVersion) {
        this.coreBuildVersion=coreBuildVersion;
        return this;
    }

    public ClusterBuilder withTransport(TransportBuilder transportBuilder) {
        this.transportBuilder=transportBuilder;
        return this;
    }

    public ClusterBuilder withComponent(ComponentBuilder componentBuilder) {
        if (componentBuilders ==null) componentBuilders = new HashSet<ComponentBuilder>();
        componentBuilders.add(componentBuilder);
        return this;
    }

    public ClusterBuilder withComponentIfNotExist(ComponentBuilder componentBuilder) {
        if (!containComponent(componentBuilder.classRole)) {
            if (componentBuilders ==null) componentBuilders = new HashSet<ComponentBuilder>();
            componentBuilders.add(componentBuilder);
        }
        return this;
    }

    public boolean containComponent(Class<? extends Component> classComponent) {
        if (componentBuilders ==null) return false;
        for (ComponentBuilder componentBuilder: componentBuilders) {
            if (componentBuilder.classRole == classComponent) return true;
        }
        return false;
    }

    public Cluster build() throws ReflectiveOperationException {
        TransportManager transportManager = transportBuilder.build();

        Cluster cluster = new Cluster(transportManager);
        LoaderComponents loaderRoles = cluster.getLoaderComponents();

        //Инициализируем все роли
        if (componentBuilders !=null) {
            Set<ComponentBuilder> waitInitRoles = new HashSet<>(componentBuilders);
            while(!waitInitRoles.isEmpty()) {

                //Ищем какую роль можно загрузить
                ComponentBuilder nextLoadingRole=null;
                for( ComponentBuilder iRoleBuilder: waitInitRoles) {
                    //Проверяем все ли зависимости загружены
                    boolean isSuccessDependencies=true;
                    for (Class<? extends Component> iDependency: iRoleBuilder.info.getDependencies()) {
                        if (loaderRoles.getAnyComponent(iDependency)==null) {
                            isSuccessDependencies=false;
                            break;
                        }
                    }
                    if (isSuccessDependencies) {
                        nextLoadingRole = iRoleBuilder;
                        break;
                    }
                }
                if (nextLoadingRole==null) throw new RuntimeException("Can not be allowed to cycle dependencies: [" + String.join(", ", waitInitRoles.stream().map(ComponentBuilder::toString).collect(Collectors.toList())) + "]");

                //Проверяем версию
                if (coreBuildVersion!=null && nextLoadingRole.info.getCoreBuildVersion()!=null) {

                }

                //Загружаем
                cluster.getLoaderComponents().initComponent(nextLoadingRole.classRole, nextLoadingRole.getConfig());

                waitInitRoles.remove(nextLoadingRole);
            }
        }

        return cluster;
    }
}
