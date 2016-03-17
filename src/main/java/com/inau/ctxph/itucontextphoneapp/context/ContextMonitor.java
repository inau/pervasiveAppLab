package com.inau.ctxph.itucontextphoneapp.context;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.inau.ctxph.itucontextphoneapp.data.local.entities.ContextEntity;

import java.util.Date;

/**
 * Created by Ivan on 03-03-2016.
 */
public class ContextMonitor implements SensorEventListener {
    protected Sensor _sensor;
    protected float[] _data;

    ContextMonitor(Sensor sensor) {
        this._sensor = sensor;
    }

    public ContextEntity toEntity() {
        ContextEntity ctxe = new ContextEntity();
        ctxe.setSensor(_sensor.getName());
        ctxe.setType( _sensor.getStringType() );
        ctxe.setTimeStamp(new Date());
        return ctxe;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if( event.sensor.equals(_sensor) ) {
            _data = event.values;
        }
    }

    public Sensor getAssociatedSensor() {
        return _sensor;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public static class AcceleroMeterMonitor extends ContextMonitor {
        public AcceleroMeterMonitor(Sensor sensor) {
            super(sensor);
        }

        @Override
        public ContextEntity toEntity() {
            ContextEntity ctxe = super.toEntity();
            ctxe.setValue( dataFilter( _data ).toString() );
            return ctxe;
        }

        private float[] dataFilter(float[] raw) {
            float[] gravity = new float[raw.length];

            // In this example, alpha is calculated as t / (t + dT),
            // where t is the low-pass filter's time-constant and
            // dT is the event delivery rate.
            final float alpha = 0.8f;

            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * raw[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * raw[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * raw[2];

            // Remove the gravity contribution with the high-pass filter.
            gravity[0] = raw[0] - gravity[0];
            gravity[1] = raw[1] - gravity[1];
            gravity[2] = raw[2] - gravity[2];

            return gravity;
        }
    }

}
