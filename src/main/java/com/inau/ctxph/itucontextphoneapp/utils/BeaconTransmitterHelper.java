package com.inau.ctxph.itucontextphoneapp.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.inau.ctxph.itucontextphoneapp.R;
import com.inau.ctxph.itucontextphoneapp.fragments.BeaconMonitoringFragment;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ivan on 18-02-2016.
 */
public class BeaconTransmitterHelper {
    public static final String TAG = "beacontransmitterhelper";

    static Beacon mBeacon;
    static BeaconTransmitter mBeaconTransmitter;

    public static void begin(final Activity c) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (c.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle(R.string.request_permission_location_title);
                builder.setMessage(R.string.request_permission_location_body);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(
                        new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                c.requestPermissions(
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        BeaconMonitoringFragment.PERMISSION_REQUEST_COARSE_LOCATION
                                );
                            }
                        }
                );
                builder.show();
            }

        mBeacon = mBeacon == null
                ? new Beacon.Builder()
                    .setId1("E3B54450AB734D7985D6 519EAF0F45D9")
                    .setId2("666")
                    .setId3("1337")
                    .setManufacturer(0x0075)
                    .setTxPower(-59)
                    .setServiceUuid(9999)
                    .build()
                : mBeacon;
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");

        mBeaconTransmitter = mBeaconTransmitter == null
                ? new BeaconTransmitter( c.getApplicationContext(), beaconParser)
                : mBeaconTransmitter;

            mBeaconTransmitter.setBeacon(mBeacon);
        mBeaconTransmitter.startAdvertising(mBeacon);
        }
    }

    public static void stop() {
        if(mBeacon != null ) mBeaconTransmitter.stopAdvertising();
    }

}
