package com.sony.mexi.orb.android.service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServlet;

import com.sony.mexi.orb.server.OrbServer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class OrbAndroidService extends Service {

    protected OrbServer server = null;
    protected ExecutorService pool = null;

    @Override
    @SuppressWarnings("unchecked")
    public IBinder onBind(Intent intent) {
        Map<String, HttpServlet> servlets = (Map<String, HttpServlet>) intent.getSerializableExtra("servlets");
        pool = Executors.newFixedThreadPool(intent.getIntExtra("numThreads", 2));
        server = new OrbServer(
                    servlets,
                    intent.getIntExtra("port", 10000),
                    pool,
                    intent.getIntExtra("backlog", 10),
                    intent.getBooleanExtra("autoServiceGuide", false));
        server.start();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        server.finish();
        if (pool != null) {
            pool.shutdown();
            while (!pool.isTerminated()) {
                try {
                    pool.awaitTermination(60, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        server.shutdown();
        return super.onUnbind(intent);
    }

}
