package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.struct.config.ComponentConfigBuilder;
import com.infomaximum.cluster.utils.version.AppVersion;
import com.infomaximum.cluster.utils.version.VersionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by kris on 20.06.17.
 */
public class Info {

    private final String uuid;
    private final Class<? extends Component> classRole;

    private final String version;
    private final boolean autoStart;
    private final boolean supportMaintenance;
    private final Class<? extends Component>[] dependencies;
    private final String coreBuildVersion;
    private final Class<? extends ComponentConfigBuilder> classRoleConfigBuilder;

    private Info(Builder builder) {
        this.uuid = builder.uuid;
        this.classRole = builder.classRole;
        this.version = builder.version;
        this.autoStart = builder.autoStart;
        this.supportMaintenance = builder.supportMaintenance;
        this.dependencies = (builder.dependencies==null)?new Class[0]:builder.dependencies.toArray(new Class[builder.dependencies.size()]);
        this.coreBuildVersion = builder.coreBuildVersion;
        this.classRoleConfigBuilder = builder.classRoleConfigBuilder;
    }

    public String getUuid() {
        return uuid;
    }
    public Class<? extends Component> getClassRole() {
        return classRole;
    }

    public String getVersion() {
        return version;
    }
    public boolean isAutoStart() {
        return autoStart;
    }
    public boolean isSupportMaintenance() {
        return supportMaintenance;
    }
    public Class<? extends Component>[] getDependencies() {
        return dependencies;
    }
    public String getCoreBuildVersion() {
        return coreBuildVersion;
    }
    public Class<? extends ComponentConfigBuilder> getClassRoleConfigBuilder() {
        return classRoleConfigBuilder;
    }

    public static class Builder {

        private final static Logger log = LoggerFactory.getLogger(Builder.class);

        private final String uuid;
        private final Class<? extends Component> classRole;

        private final String version;

        private boolean autoStart = true;
        private boolean supportMaintenance = false;
        private Set<Class<? extends Component>> dependencies;
        private String coreBuildVersion;
        private Class<? extends ComponentConfigBuilder> classRoleConfigBuilder;

        public Builder(Class<? extends Component> classRole) {
            this.classRole = classRole;
            this.uuid=classRole.getPackage().getName();

            String fileNameInfo = uuid + ".info";
            try (InputStream isInfo = getClass().getClassLoader().getResourceAsStream(fileNameInfo)) {
                if (isInfo!=null) {
                    Properties properties = new Properties();
                    properties.load(isInfo);

                    for (Object oKey: properties.keySet()) {
                        String key = (String) oKey;
                        switch (key) {
//                            case "version":
//                                withVersion(properties.getProperty(key));
//                                break;
                            default:
                                throw new RuntimeException("Not support key: " + key);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Exception parse file info: " + uuid, e);
            }

            //Загружаем версию
            this.version = AppVersion.getVersion(classRole);
            try {
                VersionUtils.validateVersion(version);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка валидации версии, подсистемы: " + uuid, e);
            }
        }

        public Builder withAutoStart(boolean autoStart) {
            this.autoStart = autoStart;
            return this;
        }

        public Builder withSupportMaintenance(boolean supportMaintenance) {
            this.supportMaintenance = supportMaintenance;
            return this;
        }

        public Builder withDependence(Class<? extends Component> dependence){
            if (dependencies==null) {
                dependencies = new HashSet<Class<? extends Component>>();
            }
            dependencies.add(dependence);
            return this;
        }

        public Builder withCoreBuildVersion(String coreBuildVersion) {
            this.coreBuildVersion=coreBuildVersion;
            return this;
        }

        public Builder withClassRoleConfigBuilder(Class<? extends ComponentConfigBuilder> value) {
            this.classRoleConfigBuilder = value;
            return this;
        }

        public Info build(){
            return new Info(this);
        }
    }
}
