package com.xl.projectno.base;

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <br>类描述:
 * <br>功能详细描述:
 *
 * @author linhang
 * @date [2013-12-17]
 */
public class CustomThread extends Thread {

    private static final String DEFAULT_THREAD_NAME = "CustomThread";
    private static final String TAG = "CustomThread";


    private static final AtomicInteger CREATED = new AtomicInteger();
    private static final AtomicInteger ALIVE = new AtomicInteger();
    private static final boolean DEBUG = false;


    public CustomThread(Runnable runnable) {
        this(runnable, DEFAULT_THREAD_NAME);
    }

    public CustomThread(Runnable runnable, String threadName) {

        super(runnable, threadName + "-" + CREATED.incrementAndGet());
        setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.w(TAG, "ThreadName: " + thread.getName(), ex);
            }
        });

    }

    @Override
    public void run() {

        if (DEBUG) {
            Log.e(TAG, "Created " + getName());
        }
        try {
            ALIVE.incrementAndGet();
            super.run();
        } finally {
            ALIVE.decrementAndGet();
            if (DEBUG) {
                Log.w(TAG, "Exiting " + getName());
            }
        }

    }

    public static int getThreadCreated() {
        return CREATED.get();
    }

    public static int getThreadAlive() {
        return ALIVE.get();
    }

}
