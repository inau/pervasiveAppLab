package com.inau.ctxph.itucontextphoneapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.inau.ctxph.itucontextphoneapp.beacons.BeaconReferenceApplication;
import com.inau.ctxph.itucontextphoneapp.data.provider.BeaconProvider;
import com.inau.ctxph.itucontextphoneapp.fragments.MapFragment;
import com.inau.ctxph.itucontextphoneapp.fragments.BeaconMonitoringFragment;
import com.inau.ctxph.itucontextphoneapp.service.ContextService;
import com.inau.ctxph.itucontextphoneapp.utils.BeaconTransmitterHelper;
import com.inau.ctxph.itucontextphoneapp.utils.Q;
import com.inau.ctxph.itucontextphoneapp.utils.Ui;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan on 11-02-2016.
 */
public class MainActivity extends FragmentActivity {

    private boolean isTransmitting;

    FrameLayout fragmentPlaceholder;
    BeaconMonitoringFragment beaconFrag;
    MapFragment mapFrag;

    Map<String, Ui.CompositeView> complex = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Q.SQL.open(this);

        //placeholder
        fragmentPlaceholder = (FrameLayout) findViewById(R.id.main_fragment_placeholder);

        //complex views (buttons first)
        complex.put(BeaconMonitoringFragment.TAG,
                new Ui.CompositeView(   findViewById(R.id.main_btn_list),
                                        findViewById(R.id.main_pic_list)
                )
        );
        complex.put(MapFragment.TAG,
                new Ui.CompositeView(   findViewById(R.id.main_btn_map),
                                        findViewById(R.id.main_pic_map)
                )
        );
        complex.put(BeaconTransmitterHelper.TAG,
                new Ui.CompositeView(   findViewById(R.id.main_btn_beacon),
                                        findViewById(R.id.main_pic_beacon),
                                        findViewById(R.id.main_glow_beacon)
                )
        );

        buttonListeners();

        //find fragment
        beaconFrag = (BeaconMonitoringFragment) getSupportFragmentManager().findFragmentByTag(BeaconMonitoringFragment.TAG);
        mapFrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);

        //check for nulls
        beaconFrag = beaconFrag == null ? new BeaconMonitoringFragment() : beaconFrag;
        mapFrag = mapFrag == null ? new MapFragment() : mapFrag;

        //initial setup
        complex.get(BeaconTransmitterHelper.TAG).highlightedComponents(isTransmitting);
        enableButton(MapFragment.TAG);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentPlaceholder.getId(), mapFrag, MapFragment.TAG).addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ( (BeaconReferenceApplication) this.getApplicationContext() ).setMonitoringActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconReferenceApplication)this.getApplicationContext()).setMonitoringActivity(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Q.SQL.close();
    }

    private void buttonListeners() {
        for (final Map.Entry<String, Ui.CompositeView> e : complex.entrySet() ) {
            //remember default onclicklistener on complex-view is view 0 (buttons in my case)
            e.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId() == e.getValue().getId() )
                        handleButtonEvent(e.getKey());
                }
            });
        }
    }

    private void handleButtonEvent(String tag) {
        android.support.v4.app.Fragment f;
        switch (tag) {
            case MapFragment.TAG:
                f = mapFrag;
                break;
            case BeaconMonitoringFragment.TAG:
                f = beaconFrag;
                break;
            case BeaconTransmitterHelper.TAG:
                toggleBeacon();
                return;
            default:
                //if unknown tag do nothing
                return;
        }

        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(fragmentPlaceholder.getId(), f, tag).commit();
        enableButton(tag);
    }

    private void toggleBeacon() {
        if( !isTransmitting ) {
            startService( new Intent(this, ContextService.class) );
            Log.i(BeaconTransmitterHelper.TAG, "Start");
            BeaconTransmitterHelper.begin(this);
            isTransmitting = true;
        }
        else {
            stopService( new Intent(this, ContextService.class) );
            Log.i(BeaconTransmitterHelper.TAG, "end");
            BeaconTransmitterHelper.stop();
            isTransmitting = false;
        }
        complex.get(BeaconTransmitterHelper.TAG).highlightedComponents(isTransmitting);

    }


    private void enableButton(String tag) {
        for(Map.Entry<String, Ui.CompositeView> entry : complex.entrySet() ) {
            if(entry.getKey() == BeaconTransmitterHelper.TAG) continue; //skip the beacon button
            entry.getValue().highlightedComponents( tag == entry.getKey() );
        }
    }

}
