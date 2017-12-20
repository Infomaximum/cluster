package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.Version;

/**
 * Реализация этого интерейса будет определять политику "поддержания" версий компонентов
 */
public interface ControlComponentVersion {

    /**
     * Возврощае
     *
     * @return
     */
    boolean isSupportVersion(Version environmentVersion, Version componentEnvironmentVersion);
}
