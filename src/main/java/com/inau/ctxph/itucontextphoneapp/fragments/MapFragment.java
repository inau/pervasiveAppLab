package com.inau.ctxph.itucontextphoneapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inau.ctxph.itucontextphoneapp.R;
import com.inau.ctxph.itucontextphoneapp.map.MapHandler;

/**
 * Created by Ivan on 11-02-2016.
 */
public class MapFragment extends SupportMapFragment {
    public static final String TAG = "mapfragmenttag";

    OnMapReadyCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        callback = MapHandler.getInstance( getActivity() );
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(callback);
    }



}
