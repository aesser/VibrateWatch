package com.example.alexander.vibratewatch;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alexander on 04.11.2015.
 */
public class NotificationRunner {
        private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void start(){
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Debugger.log("refreshed " + new Date().toString());

            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}
