package com.inau.ctxph.itucontextphoneapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.inau.ctxph.itucontextphoneapp.context.ContextMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 03-03-2016.
 */
public class ContextService extends IntentService {

    SensorManager mSensorManager;
    List<ContextMonitor> _activeMonitors = new ArrayList<>();
    Thread t;

    public ContextService(String name) {
        super(name);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Setup
        ContextMonitor c = new ContextMonitor
                .AcceleroMeterMonitor( mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) );
        registerMonitor(c);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i = super.onStartCommand(intent, flags, startId);


        t = t == null ?  new Thread(
        new Runnable() {
            @Override
            public void run() {
                for (ContextMonitor cm : _activeMonitors)
                    Log.i("cntx", ""+cm.toEntity() );
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }) : t;
        if (!t.isAlive()) t.start();

        return i;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        for (ContextMonitor m : _activeMonitors) unregisterMonitor(m);
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        for (ContextMonitor m : _activeMonitors) registerMonitor(m);
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.interrupt();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public void registerMonitor(ContextMonitor monitor) {
        _activeMonitors.add(monitor);
        mSensorManager.registerListener(monitor, monitor.getAssociatedSensor(), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterMonitor(ContextMonitor monitor) {
        _activeMonitors.remove(monitor);
        mSensorManager.unregisterListener(monitor);
    }

}
