package com.example.eriko.updateditmmockup.activities;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.ImageView;

import com.example.eriko.updateditmmockup.R;
import com.example.eriko.updateditmmockup.classes.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class Ticket extends AppCompatActivity implements BeaconConsumer {

    private final String TAG = this.getClass().getName();

    private BeaconManager beaconManager;
    private User user;
    ImageView imageView;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        user = (User) getIntent().getSerializableExtra("user");

        imageView = findViewById(R.id.imageView);
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        Log.d(TAG, "Logged in: " + MainActivity.loggedIn);
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            if (MainActivity.loggedIn) {
                for (int x = 0; x < bitMatrixWidth; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ?
                            getColor(R.color.totalBlack):getColor(R.color.green);
                }
            } else {
                for (int x = 0; x < bitMatrixWidth; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ?
                            getColor(R.color.totalBlack):getColor(R.color.red);
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, QRcodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public void onBeaconServiceConnect() {
        final Region region1 = new Region ("beacon", Identifier.parse("6e42f68a-d0d1-467b-a23e-9d11fa746e43"), Identifier.parse("0160"), Identifier.parse("0106"));

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    Ticket.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

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
                    Ticket.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

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
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                Ticket.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bitmap = TextToImageEncode(user.getEmail());
                            imageView.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
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