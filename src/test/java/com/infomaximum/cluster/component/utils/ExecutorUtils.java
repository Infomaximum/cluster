package com.infomaximum.cluster.component.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by user on 06.01.2016.
 */
public class ExecutorUtils {

    public static final ExecutorService executors = Executors.newCachedThreadPool();
}
