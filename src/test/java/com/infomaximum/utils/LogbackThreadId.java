package com.infomaximum.utils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by user on 02.12.2015.
 */
public class LogbackThreadId extends ClassicConverter {

    private static AtomicLong nextId = new AtomicLong(0);
    private static final ThreadLocal<String> threadId = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return String.format("%08d", nextId.incrementAndGet());
        }
    };

    @Override
    public String convert(ILoggingEvent event) {
        return threadId.get();
    }
}
