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

    private Set<ComponentBuilder> roleBuilders;
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

    public ClusterBuilder withRole(ComponentBuilder roleBuilder) {
        if (roleBuilders==null) roleBuilders = new HashSet<ComponentBuilder>();
        roleBuilders.add(roleBuilder);
        return this;
    }

    public ClusterBuilder withRoleIfNotExist(ComponentBuilder roleBuilder) {
        if (!containRole(roleBuilder.classRole)) {
            if (roleBuilders==null) roleBuilders = new HashSet<ComponentBuilder>();
            roleBuilders.add(roleBuilder);
        }
        return this;
    }

    public boolean containRole(Class<? extends Component> classRole) {
        if (roleBuilders==null) return false;
        for (ComponentBuilder roleBuilder: roleBuilders) {
            if (roleBuilder.classRole == classRole) return true;
        }
        return false;
    }

    public Cluster build() throws Exception {
        TransportManager transportManager = transportBuilder.build();

        Cluster cluster = new Cluster(transportManager);
        LoaderComponents loaderRoles = cluster.getLoaderRoles();

        //Инициализируем все роли
        if (roleBuilders!=null) {
            Set<ComponentBuilder> waitInitRoles = new HashSet<>(roleBuilders);
            while(!waitInitRoles.isEmpty()) {

                //Ищем какую роль можно загрузить
                ComponentBuilder nextLoadingRole=null;
                for( ComponentBuilder iRoleBuilder: waitInitRoles) {
                    //Проверяем все ли зависимости загружены
                    boolean isSuccessDependencies=true;
                    for (Class<? extends Component> iDependency: iRoleBuilder.info.getDependencies()) {
                        if (loaderRoles.getAnyRole(iDependency)==null) {
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
                cluster.getLoaderRoles().initRole(nextLoadingRole.classRole, nextLoadingRole.getConfig());

                waitInitRoles.remove(nextLoadingRole);
            }
        }

        return cluster;
    }
}
