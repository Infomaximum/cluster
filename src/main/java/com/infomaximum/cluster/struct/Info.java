package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.Version;
import com.infomaximum.cluster.struct.config.ComponentConfigBuilder;
import com.infomaximum.cluster.utils.version.AppVersion;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kris on 20.06.17.
 */
public class Info {

    private final Class<? extends Component> componentClass;
    private final String uuid;
    private final Version version;
    private final Class<? extends Component>[] dependencies;
    private final Version environmentVersion;
    private final Class<? extends ComponentConfigBuilder> configBuilderClass;

    private Info(Builder builder) {
        this.componentClass = builder.componentClass;
        this.uuid = componentClass.getPackage().getName();
        this.version = AppVersion.getVersion(componentClass);
        this.dependencies = builder.dependencies == null ? new Class[0] : builder.dependencies.toArray(new Class[builder.dependencies.size()]);
        this.environmentVersion = builder.environmentVersion;
        this.configBuilderClass = builder.configBuilderClass;
    }

    public String getUuid() {
        return uuid;
    }

    public Class<? extends Component> getComponent() {
        return componentClass;
    }

    public Version getVersion() {
        return version;
    }

    public Class<? extends Component>[] getDependencies() {
        return dependencies;
    }

    public Version getEnvironmentVersion() {
        return environmentVersion;
    }

    public Class<? extends ComponentConfigBuilder> getConfigBuilder() {
        return configBuilderClass;
    }

    public boolean isCompatibleWith(Version targetEnvironmentVersion) {
        return environmentVersion.major == targetEnvironmentVersion.major &&
                environmentVersion.minor == targetEnvironmentVersion.minor &&
                environmentVersion.build >= targetEnvironmentVersion.build;
    }

    public static class Builder {

        private final Class<? extends Component> componentClass;

        private Set<Class<? extends Component>> dependencies = new HashSet<>();
        private Version environmentVersion;
        private Class<? extends ComponentConfigBuilder> configBuilderClass;

        public Builder(Class<? extends Component> componentClass) {
            this.componentClass = componentClass;
        }

        public Builder withDependence(Class<? extends Component> dependence){
            dependencies.add(dependence);
            return this;
        }

        public Builder withEnvironmentVersion(Version environmentVersion) {
            this.environmentVersion = environmentVersion;
            return this;
        }

        public Builder withConfigBuilder(Class<? extends ComponentConfigBuilder> value) {
            this.configBuilderClass = value;
            return this;
        }

        public Info build(){
            return new Info(this);
        }
    }
}
