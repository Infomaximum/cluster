package com.infomaximum.cluster.struct;

/**
 * Реализация этого интерейса будет определять политику "поддержания" версий компонентов
 */
public interface ControlComponentVersion {

    /**
     * Возврощае
     *
     * @return
     */
    boolean isSupportVersion(Info info);
}
