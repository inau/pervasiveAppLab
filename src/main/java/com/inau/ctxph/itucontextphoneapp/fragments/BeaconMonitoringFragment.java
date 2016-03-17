package com.inau.ctxph.itucontextphoneapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.inau.ctxph.itucontextphoneapp.R;
import com.inau.ctxph.itucontextphoneapp.beacons.BeaconReferenceApplication;
import com.inau.ctxph.itucontextphoneapp.data.adaptor.BeaconAdaptor;
import com.inau.ctxph.itucontextphoneapp.data.local.entities.Beacon;
import com.inau.ctxph.itucontextphoneapp.utils.Q;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ivan on 11-02-2016.
 */
public class BeaconMonitoringFragment extends ListFragment implements BeaconConsumer, Observer {
    public static final String ITU_UUID = "E3B54450-AB73-4D79-85D6-519EAF0F45D9";
    public static final String TAG = "beaconfragmenttag";
    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private boolean ituOnly = false;
    private boolean bulk = false;
    private BeaconManager mBeaconManager;
    private BeaconAdaptor adaptor = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBeaconManager = BeaconManager.getInstanceForApplication( getActivity() );
       // verifyBluetooth();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if( getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                final AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
                builder.setTitle(R.string.request_permission_location_title);
                builder.setMessage(R.string.request_permission_location_body);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(
                        new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                requestPermissions(
                                        new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION },
                                        PERMISSION_REQUEST_COARSE_LOCATION
                                );
                            }
                        }
                );
                builder.show();
            }

        }
        mBeaconManager.bind(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Q.SQL.BEACONS.addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Q.SQL.BEACONS.deleteObserver(this);
        mBeaconManager.unbind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_beacon_fragment, container, false);

        final RadioButton   all = (RadioButton) v.findViewById(R.id.lbc_rad_grp_all),
                            itu = (RadioButton) v.findViewById(R.id.lbc_rad_grp_itu);

        final RadioGroup rgroup = (RadioGroup) v.findViewById(R.id.lbc_rad_grp);

        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 if(rgroup == group) {
                     if(all.getId() == checkedId) ituOnly = false;
                     if(itu.getId() == checkedId) ituOnly = true;

                     updateListView();
                 }
            }
        });

        adaptor = new BeaconAdaptor(v.getContext(), Q.SQL.BEACONS.getAllBeacons() );
        setListAdapter(adaptor);

        return v;
    }

    private void updateListView() {
        if(bulk) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adaptor.setData(ituOnly ? Q.SQL.BEACONS.findBeaconsWithUuid(ITU_UUID)
                                        : Q.SQL.BEACONS.getAllBeacons()
                );
                adaptor.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data instanceof Q.ObserverData) {
            switch ( ((Q.ObserverData) data).getType() ) {
                case SQL:
                    updateListView();
                    break;
                case WEB:
                    Beacon[] res = (Beacon[]) ((Q.ObserverData) data).getData();
                    for (Beacon g : res) {
                        try {
                            Q.SQL.BEACONS.createFromObject(g);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (java.lang.InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                    updateListView();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
                    builder.setTitle(R.string.request_permission_limit);
                    builder.setMessage(R.string.request_permission_location_negative);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
        }
    }

    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this.getActivity()).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getActivity().finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    getActivity().finish();
                    System.exit(0);
                }

            });
            builder.show();

        }

    }

    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.setMonitorNotifier(( new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "sight");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "hidden");
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.i(TAG, "state");
            }
        }));

        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<org.altbeacon.beacon.Beacon> collection, Region region) {
                region.getUniqueId();
                Log.i(TAG, "Found beacons: "+collection.size());
                bulk = true; int it = collection.size();
                for (org.altbeacon.beacon.Beacon b : collection) {
                    // Log.i(TAG,  "beacon: "+b.getId1() +
                    //             " " +b.getId2() +
                    //             " " + b.getId3() +
                    //             " " + b.getBeaconTypeCode() +
                    //             " " + b.getManufacturer()
                    // );
                    it--;
                    if(it == 0) bulk = false;
                    try {
                        Q.SQL.BEACONS.createBeacon(null, "" + b.getId1(), "" + b.getId2(), "" + b.getId3(),
                                "" + b.getBeaconTypeCode(), "" + b.getManufacturer(),
                                b.getDataFields().toString(), b.getDistance()
                        );
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("E3B54450AB734D7985D6 519EAF0F45D9", null, null, null));
            mBeaconManager.setForegroundScanPeriod(2000);
            mBeaconManager.setBackgroundScanPeriod(8000);
        } catch (RemoteException e) {    }
    }

    @Override
    public Context getApplicationContext() {
        return getContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }

}
