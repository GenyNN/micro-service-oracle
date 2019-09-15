package com.tallink.clientmatching.util;

import org.springframework.util.StopWatch;

import java.util.function.Supplier;

public class InstrumentationUtils {

    public static <T> T executeAndLogTime(String tag, Supplier<T> function) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        T result = function.get();
        stopWatch.stop();
        System.out.println(tag + ": " + stopWatch.getTotalTimeMillis());
        return result;
    }
}
