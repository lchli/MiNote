package com.lch.menote.note;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lichenghang on 2018/5/19.
 */

public final class TaskExecutor {

    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void execute(Runnable r) {
        executor.execute(r);
    }
}
