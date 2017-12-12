package com.example.eriko.updateditmmockup.Classes;

import android.Manifest;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 * Created by Eriko on 2017-12-07.
 */

public class BeaconConnection extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "tag";
    private BeaconManager beaconManager;
    TextView beaconState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        beaconState.setText("state");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    public void onBeaconServiceConnect() {
        final Region region1 = new Region ("beacon", Identifier.parse("6e42f68a-d0d1-467b-a23e-9d11fa746e43"), Identifier.parse("0160"), Identifier.parse("0106"));

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    Log.d(TAG, "didEnterRegion");
                    BeaconConnection.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            beaconState.setText("didEnterRegion");
                        }
                    });
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    Log.d(TAG, "didExitRegion");
                    BeaconConnection.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            beaconState.setText("didExitRegion");
                        }
                    });
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(final int i, Region region) {
                try {
                    Log.d(TAG, "didDetermineStateForRegion");
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e){
                    e.printStackTrace();
                }

                BeaconConnection.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        beaconState.setText("didDetermineStateForRegion " + i);

                    }
                });
            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for(Beacon oneBeacon : beacons) {
                    Log.d(TAG, "distance: " + oneBeacon.getDistance() + " id:" + oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());
                }
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}