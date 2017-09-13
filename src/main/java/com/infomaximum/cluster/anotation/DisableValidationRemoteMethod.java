package com.infomaximum.cluster.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by user on 19.04.2017.
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface DisableValidationRemoteMethod {

}