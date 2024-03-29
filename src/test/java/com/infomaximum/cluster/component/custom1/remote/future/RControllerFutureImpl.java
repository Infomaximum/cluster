package com.infomaximum.cluster.component.custom1.remote.future;

import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.utils.ExecutorUtils;
import com.infomaximum.cluster.core.remote.AbstractRController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.NotLinkException;
import java.util.concurrent.CompletableFuture;

/**
 * Created by kris on 28.10.16.
 */
public class RControllerFutureImpl extends AbstractRController<Custom1Component> implements RControllerFuture {

    private final static Logger log = LoggerFactory.getLogger(RControllerFutureImpl.class);

    public RControllerFutureImpl(Custom1Component component) {
        super(component);
    }

    @Override
    public CompletableFuture<String> get(String value, long time) {
        CompletableFuture<String> future = new CompletableFuture<>();

        if (time == 0) {
            future.complete(value);
        } else {
            ExecutorUtils.executors.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(time);
                        future.complete(value);
                    } catch (InterruptedException e) {
                        future.completeExceptionally(e);
                    }
                }
            });
        }

        return future;
    }

    @Override
    public CompletableFuture<String> getError(String value, long time) {
        CompletableFuture<String> future = new CompletableFuture<>();

        if (time == 0) {
            future.completeExceptionally(new NotLinkException("fake"));
        } else {
            ExecutorUtils.executors.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(time);
                        future.completeExceptionally(new NotLinkException("fake"));
                    } catch (InterruptedException e) {
                        future.completeExceptionally(e);
                    }
                }
            });
        }

        return future;
    }
}
