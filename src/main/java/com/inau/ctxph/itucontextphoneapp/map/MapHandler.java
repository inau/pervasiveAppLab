package com.inau.ctxph.itucontextphoneapp.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Provider;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 11-02-2016.
 */
public class MapHandler implements OnMapReadyCallback, LocationListener {
    static final String tag = "maphandler";

    private static MapHandler mInstance = null;

    public static MapHandler getInstance(Context c) {
        if(mInstance == null) {
            mInstance = new MapHandler( c.getApplicationContext() );
        }
        return mInstance;
    }

    private MapHandler(Context c) {
        this.c = c;
    }

    GoogleMap mMap;
    Context c;
    LocationManager mLocationManager;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(tag, "MapReady");
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        mLocationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        Log.i(tag, "Location Manager exists: " + (mLocationManager != null));

        if (    ActivityCompat.checkSelfPermission(c,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(c,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.i(tag, "Missing permissions");
            return;
        }
        mMap.clear();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
                0.1f, this);

        List<String> providers = mLocationManager.getProviders(true);

        Location l = null;
        for (String provider : providers) {
            l = mLocationManager.getLastKnownLocation(provider);
        }
        Log.i(tag, "LastKnownLocation exists: " + (l != null) );

        // Location.
        LatLng coords = l == null ? new LatLng(-34, 151) : new LatLng(l.getLatitude(), l.getLongitude());
        String label = l == null ? "Down Under" : "You are here";

        mMap.addMarker(new MarkerOptions().position(coords).title(label));
        centerOnLocation(coords);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(tag, "Location callback");
        mMap.clear();
        LatLng update = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker( new MarkerOptions()
                            .position(update)
                                .title("Current Position")
        );
        //centerOnLocation(update);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(tag, "status callback");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(tag, "provider callback enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(tag, "provider callback disabled");
    }

    private void centerOnLocation(LatLng coords) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords,18));
    }
}
