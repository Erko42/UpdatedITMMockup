package com.example.eriko.updateditmmockup.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.eriko.updateditmmockup.R;
import com.example.eriko.updateditmmockup.classes.RESTManager;
import com.example.eriko.updateditmmockup.helpers.DatabaseHelper;
import com.example.eriko.updateditmmockup.interfaces.CustomerVolleyArrayCallback;
import com.example.eriko.updateditmmockup.interfaces.VolleyArrayListCallback;
import com.example.eriko.updateditmmockup.interfaces.VolleyJsonObjectCallback;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private final String TAG = this.getClass().getName();

    public static boolean checkedIn;
    private BeaconManager beaconManager;

    DatabaseHelper db;
    HashMap<String, String> hashMap;

    RequestQueue requestQueue;
    RESTManager restmanager;

    ConstraintLayout constraintLayout;

    EditText theCodeView;
    TextView readyToDownloadText;
    TextView codeNumber1Place;
    TextView codeNumber2Place;
    TextView codeNumber3Place;
    TextView codeNumber4Place;

    Animation animationslidedown;
    Animation animationslideup;
    ValueAnimator animToWhite1;
    ValueAnimator animToWhite2;
    ValueAnimator animToWhite3;
    ValueAnimator animToWhite4;
    ValueAnimator animToGrey1;
    ValueAnimator animToGrey2;
    ValueAnimator animToGrey3;
    ValueAnimator animToGrey4;

    ImageView infoImage;
    ImageView downloadImage;

    int code;
    int colorSwitchDuration;
    int progress;

    boolean isSlided;
    boolean oneIsSet;
    boolean twoIsSet;
    boolean threeIsSet;
    boolean fourIsSet;
    boolean infoIsPressed;
    boolean downloadIsPressed;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        checkedIn = false;

        db = new DatabaseHelper(this);
        hashMap = new HashMap<>();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        restmanager = new RESTManager(this, requestQueue);

        constraintLayout = findViewById(R.id.constraint);

        theCodeView = findViewById(R.id.theCodeText);

        readyToDownloadText = findViewById(R.id.readyForDownloadText);
        codeNumber1Place = findViewById(R.id.codeNumber1Place);
        codeNumber2Place = findViewById(R.id.codeNumber2Place);
        codeNumber3Place = findViewById(R.id.codeNumber3Place);
        codeNumber4Place = findViewById(R.id.codeNumber4Place);

        infoImage = findViewById(R.id.infoButton);
        downloadImage = findViewById(R.id.downloadButton);

        infoImage.setEnabled(false);
        downloadImage.setEnabled(false);

        animationslidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animationslideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        colorSwitchDuration = 300;
        progress = 0;

        oneIsSet = false;
        twoIsSet = false;
        threeIsSet = false;
        fourIsSet = false;

        db.deleteAllData(DatabaseHelper.USER_TABLE);
        db.deleteAllData(DatabaseHelper.EVENT_TABLE);

        animToWhite1 = ValueAnimator.ofArgb(Color.parseColor("#292929"), Color.parseColor("#ffffff"));
        animToWhite1.setDuration(colorSwitchDuration);
        animToWhite1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                codeNumber1Place.setTextColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        animToWhite2 = ValueAnimator.ofArgb(Color.parseColor("#292929"), Color.parseColor("#ffffff"));
        animToWhite2.setDuration(colorSwitchDuration);
        animToWhite2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                codeNumber2Place.setTextColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        animToWhite3 = ValueAnimator.ofArgb(Color.parseColor("#292929"), Color.parseColor("#ffffff"));
        animToWhite3.setDuration(colorSwitchDuration);
        animToWhite3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                codeNumber3Place.setTextColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        animToWhite4 = ValueAnimator.ofArgb(Color.parseColor("#292929"), Color.parseColor("#ffffff"));
        animToWhite4.setDuration(colorSwitchDuration);
        animToWhite4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                codeNumber4Place.setTextColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        animToGrey1 = ValueAnimator.ofArgb(Color.parseColor("#ffffff"), Color.parseColor("#292929"));
        animToGrey1.setDuration(colorSwitchDuration);
        animToGrey1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                codeNumber1Place.setTextColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        animToGrey2 = ValueAnimator.ofArgb(Color.parseColor("#ffffff"), Color.parseColor("#292929"));
        animToGrey2.setDuration(colorSwitchDuration);
        animToGrey2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                codeNumber2Place.setTextColor((Integer)valueAnimator.getAnimatedValue());

            }
        });
        animToGrey3 = ValueAnimator.ofArgb(Color.parseColor("#ffffff"), Color.parseColor("#292929"));
        animToGrey3.setDuration(colorSwitchDuration);
        animToGrey3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                codeNumber3Place.setTextColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        animToGrey4 = ValueAnimator.ofArgb(Color.parseColor("#ffffff"), Color.parseColor("#292929"));
        animToGrey4.setDuration(colorSwitchDuration);
        animToGrey4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                codeNumber4Place.setTextColor((Integer)valueAnimator.getAnimatedValue());
            }
        });


        theCodeView.addTextChangedListener(new TextWatcher() {
            //checks how many digits the user have inserted to show the animation below every digit
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!theCodeView.getText().toString().equals("")) {
                    code = Integer.parseInt(theCodeView.getText().toString());
                }
                if (theCodeView.getText().toString().length() == 0) {
                    oneIsSet = false;
                }
                if (theCodeView.getText().toString().length() == 1) {
                    twoIsSet = false;
                }
                if (theCodeView.getText().toString().length() == 2) {
                    threeIsSet = false;
                }
                if (theCodeView.getText().toString().length() == 3) {
                    fourIsSet = false;
                }
            }

            // checks and starts the animations below the digits
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!theCodeView.getText().toString().equals("")) {
                    code = Integer.parseInt(theCodeView.getText().toString());
                }
                if (theCodeView.getText().toString().length() == 1 && !oneIsSet) {
                    animToWhite1.start();
                    oneIsSet = true;
                }
                if (theCodeView.getText().toString().length() == 2 && !twoIsSet) {
                    animToWhite2.start();
                    twoIsSet = true;
                }
                if (theCodeView.getText().toString().length() == 3 && !threeIsSet) {
                    animToWhite3.start();
                    threeIsSet = true;
                }
                if (theCodeView.getText().toString().length() == 4 && !fourIsSet) {
                    animToWhite4.start();
                    fourIsSet = true;
                }

                // if all 4 digits is inserted the API calls to the server to get the necesary info from that Project
                // if it is a CustomerId go to the SplashScreen and download all the events for that CustomerId
                if (theCodeView.getText().toString().length() == 4) {
                    try {
                        restmanager.getCustomerStringFromJsonArrayFromUrl("https://api.itmmobile.com/customers/" + code + "/projects", "ProjectID", new CustomerVolleyArrayCallback() {

                            @Override
                            public void onSuccess(ArrayList result) {
                                RESTManager.requests = 0;
                                RESTManager.isMultiApp = true;
                                Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                                intent.putExtra("code", code);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailed(String result) {
                                restmanager.getJsonObjectFromUrl("https://api.itmmobile.com/projects/" + code, new VolleyJsonObjectCallback() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        try {
                                            hashMap.put("AppName", result.getString("AppName"));
                                            hashMap.put("CustomerID", result.getString("CustomerID"));
                                            hashMap.put("ProjectDuration", result.getString("ProjectDuration"));
                                            hashMap.put("ProjectID", result.getString("ProjectID"));
                                            hashMap.put("HideInMultiApp", 1 + "");
                                            String readyToDownloadtext = hashMap.get("AppName") + "\när nu redo för nedladdning";
                                            readyToDownloadText.setText(readyToDownloadtext);
                                            constraintLayout.startAnimation(animationslidedown);
                                            theCodeView.setEnabled(false);
                                            theCodeView.setEnabled(true);
                                            infoImage.setEnabled(true);
                                            downloadImage.setEnabled(true);
                                            isSlided = true;
                                            progress += 50;
                                            update();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isSlided) {
                    constraintLayout.startAnimation(animationslideup);
                    infoImage.setEnabled(false);
                    downloadImage.setEnabled(false);
                    isSlided = false;
                }
                if (theCodeView.getText().toString().length() == 0 && oneIsSet) {
                    animToGrey1.start();
                    oneIsSet = true;
                }
                if (theCodeView.getText().toString().length() == 1 && twoIsSet) {
                    animToGrey2.start();
                    twoIsSet = true;
                }
                if (theCodeView.getText().toString().length() == 2 && threeIsSet) {
                    animToGrey3.start();
                    threeIsSet = true;
                }
                if (theCodeView.getText().toString().length() == 3 && fourIsSet) {
                    animToGrey4.start();
                    fourIsSet = true;
                }
            }
        });
    }

    // downloads the BackgroundImg from the server and checks the download proccess
    public void update() {
        if (progress == 50){
            restmanager.getStringFromJsonArrayFromUrl("https://api.itmmobile.com/projects/" + code + "/contents", "BackgroundImg", new VolleyArrayListCallback() {
                @Override
                public void onSuccess(ArrayList result) {
                    hashMap.put("BackgroundImg", result.get(0).toString());
                    progress += 50;
                    update();
                }
            });
        }
        if (progress == 100 && infoIsPressed) {
            db.insertData(DatabaseHelper.EVENT_TABLE, hashMap);
            Intent intent = new Intent(MainActivity.this, EventInfobs.class);
            intent.putExtra("code", code);
            startActivity(intent);
        } else if (progress == 100 && downloadIsPressed) {
            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
            intent.putExtra("code", code);
            startActivity(intent);
        }
    }

    public void download(View view) {
        downloadIsPressed = true;
        update();
    }

    public void info(View view) {
        infoIsPressed = true;
        update();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    //AltBeacon sdk beacon connect service
    public void onBeaconServiceConnect() {
        final Region region1 = new Region ("beacon1", Identifier.parse("6e42f68a-d0d1-467b-a23e-9d11fa746e43"), Identifier.parse("0160"), Identifier.parse("0106"));

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    checkedIn = true;
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    MainActivity.this.runOnUiThread(new Runnable() {
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
                    if (i > 0) {
                        checkedIn = true;
                    } else {
                        checkedIn = false;
                    }
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for(Beacon beacon : beacons) {
                    Log.d(TAG, "distance: " + beacon.getDistance() + " id:" + beacon.getId1() + "/" + beacon.getId2() + "/" + beacon.getId3());
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