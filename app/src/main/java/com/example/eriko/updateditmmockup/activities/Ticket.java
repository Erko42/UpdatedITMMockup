package com.example.eriko.updateditmmockup.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eriko.updateditmmockup.R;
import com.example.eriko.updateditmmockup.classes.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class Ticket extends AppCompatActivity implements BeaconConsumer {

    private final String TAG = this.getClass().getName();

    Context context;
    private BeaconManager beaconManager;
    private User user;
    ImageView ticket;
    ImageView QRCode;
    ImageView bluetooth;
    ImageView locationService;
    TextView validOrInvalid;
    TextView checkedIn;
    TextView info;
    TextView clickOnTheTicket;
    boolean gpsIsOn;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        context = getApplicationContext();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        user = (User) getIntent().getSerializableExtra("user");

        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        gpsIsOn = false;

        ticket = findViewById(R.id.ticket);
        QRCode = findViewById(R.id.QRCode);
        bluetooth = findViewById(R.id.bluetooth);
        validOrInvalid = findViewById(R.id.validOrInvalid);
        checkedIn = findViewById(R.id.checkedIn);
        info = findViewById(R.id.info);
        clickOnTheTicket = findViewById(R.id.clickOnTheTicket);
        locationService = findViewById(R.id.location);

        try {
            bitmap = TextToImageEncode(user.getEmail());
            QRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (mBluetoothAdapter.isEnabled()) {
            bluetooth.setImageResource(R.drawable.bluetooth_on);
        } else {
            bluetooth.setImageResource(R.drawable.bluetooth_off);
        }
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);
                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            bluetooth.setImageResource(R.drawable.bluetooth_off);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            bluetooth.setImageResource(R.drawable.bluetooth_off);
                            break;
                        case BluetoothAdapter.STATE_ON:
                            bluetooth.setImageResource(R.drawable.bluetooth_on);
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            bluetooth.setImageResource(R.drawable.bluetooth_on);
                            break;
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                } else {
                    mBluetoothAdapter.enable();
                }
            }
        });
        locationService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsIsOn) {
                    turnGPSOff();
                } else {
                    turnGPSOn();
                }
            }
        });
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        Log.d(TAG, "Logged in: " + MainActivity.checkedIn);
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

            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getColor(R.color.totalBlack):getColor(R.color.QRCodeBackground);
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
                            Log.d(TAG, "didEnterRegion");
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
                            Log.d(TAG, "didExitRegion");
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
                        Log.d(TAG, "didDetermineStateForRegion " + i);
                        if (MainActivity.checkedIn) {
                            ticket.setImageResource(R.drawable.ticket_is_valid);
                            validOrInvalid.setText(R.string.ticket_is_valid);
                            checkedIn.setText(R.string.checked_in);
                            info.setText(R.string.info_valid);
                            clickOnTheTicket.setVisibility(View.VISIBLE);

                            ticket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Ticket.this, MainMenu.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            ticket.setImageResource(R.drawable.ticket_is_invalid);
                            validOrInvalid.setText(R.string.ticket_is_invalid);
                            checkedIn.setText(R.string.not_checked_in);
                            info.setText(R.string.info_invalid);
                            clickOnTheTicket.setVisibility(View.INVISIBLE);
                            ticket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
                    }
                });
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            gpsIsOn = true;
            locationService.setImageResource(R.drawable.location_on);
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            gpsIsOn = false;
            locationService.setImageResource(R.drawable.location_off);
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Ticket.this, Login.class);
        startActivity(intent);
    }
}