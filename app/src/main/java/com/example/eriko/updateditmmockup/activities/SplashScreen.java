package com.example.eriko.updateditmmockup.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.eriko.updateditmmockup.classes.Project;
import com.example.eriko.updateditmmockup.classes.RESTManager;
import com.example.eriko.updateditmmockup.helpers.DatabaseHelper;
import com.example.eriko.updateditmmockup.interfaces.VolleyArrayListCallback;
import com.example.eriko.updateditmmockup.interfaces.VolleyJsonObjectCallback;
import com.example.eriko.updateditmmockup.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    RequestQueue requestQueue;
    RESTManager restmanager;

    DatabaseHelper db;
    ArrayList<Project> dbList;
    HashMap<String, String> hashMap;

    Project project;

    DisplayMetrics metrics;

    ProgressBar progressBar;
    TextView progressView;
    TextView downloadingDataView;
    TextView itMayTakeAWhileView;
    String progressString;
    int progress;
    int code;
    int listIndex;
    int projectIndex;
    int requestsPerProject;
    Boolean exHibitorListsbsIsDownloaded;
    Boolean mainMenuIsDownloaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        restmanager = new RESTManager(getApplicationContext(), requestQueue);

        db = new DatabaseHelper(this);
        dbList = new ArrayList<>();
        hashMap = new HashMap<>();

        RESTManager.projects = 0;
        RESTManager.requests = 0;
        listIndex = 0;
        projectIndex = 0;
        requestsPerProject = 0;

        code = getIntent().getIntExtra("code", 0);
        exHibitorListsbsIsDownloaded = false;
        mainMenuIsDownloaded = false;

        progressBar = findViewById(R.id.progressBar);
        progressView = findViewById(R.id.progressView);
        downloadingDataView = findViewById(R.id.downloadingDataText);
        itMayTakeAWhileView = findViewById(R.id.itMayTakeAWhileView);
        progressString = progressBar.getProgress() + " %";
        progressView.setText(progressString);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //starts the flashy loading animation with a duration of 1 second
        downloadingDataView.post(new Runnable() {
            @Override
            public void run() {
                Animation anim = new RotateAnimation(0.0f, 360.0f,downloadingDataView.getMeasuredWidth() / 2, downloadingDataView.getMeasuredHeight() / 2);

                anim.setDuration(1000);
                progressBar.setAnimation(anim);

                int downloadingDataViewStart = metrics.widthPixels;
                int itMayTakeAWhileViewStart = -itMayTakeAWhileView.getMeasuredWidth();
                int progressBarStart = metrics.heightPixels;
                int progressViewStart = metrics.heightPixels;

                int downloadingDataViewDestination = metrics.widthPixels / 2 - downloadingDataView.getMeasuredWidth() / 2;
                int itMayTakeAWhileViewStartDestination = metrics.widthPixels / 2 - itMayTakeAWhileView.getMeasuredWidth() / 2;
                int progressBarDestination = metrics.heightPixels / 2 + progressBar.getMeasuredHeight() * 4;
                int progressViewDestination = metrics.heightPixels / 2 + progressView.getMeasuredHeight() * 3;

                ObjectAnimator downloadingDataViewSlider = ObjectAnimator.ofFloat(downloadingDataView, "x", downloadingDataViewStart, downloadingDataViewDestination);
                ObjectAnimator itMayTakeAWhileViewSlider = ObjectAnimator.ofFloat(itMayTakeAWhileView, "x", itMayTakeAWhileViewStart, itMayTakeAWhileViewStartDestination);
                ObjectAnimator progressBarSlider = ObjectAnimator.ofFloat(progressBar, "y", progressBarStart, progressBarDestination);
                ObjectAnimator progressViewSlider = ObjectAnimator.ofFloat(progressView, "y", progressViewStart, progressViewDestination);

                downloadingDataViewSlider.setDuration(1000);
                itMayTakeAWhileViewSlider.setDuration(1000);
                progressBarSlider.setDuration(1000);
                progressViewSlider.setDuration(1000);

                anim.start();
                downloadingDataViewSlider.start();
                itMayTakeAWhileViewSlider.start();
                progressBarSlider.start();
                progressViewSlider.start();
            }
        });
        updateRequests();
    }
    //downloads 1 event at a time if the code equals a customerId until all the events are downloaded
    public void updateRequests() {
        if (RESTManager.isMultiApp) {
            restmanager.getStringFromJsonArrayFromUrl("https://api.itmmobile.com/customers/" + code + "/projects", "HideInMultiApp", new VolleyArrayListCallback() {
                @Override
                public void onSuccess(ArrayList result) {
                    RESTManager.requests -= 1;
                    if (listIndex < 1) {
                        for (int i = 0; i < result.size(); i++) {
                            if(result.get(i).equals("false")) {
                                project = new Project();
                                project.setHideInMultiApp(0);
                                hashMap.put("HideInMultiApp", project.getHideInMultiApp() + "");
                                dbList.add(project);
                                RESTManager.projects += 1;
                            } else {
                                project = new Project();
                                project.setHideInMultiApp(1);
                                hashMap.put("HideInMultiApp", project.getHideInMultiApp() + "");
                                dbList.add(project);
                            }
                        }
                    }
                    restmanager.getStringFromJsonArrayFromUrl("https://api.itmmobile.com/customers/" + code + "/projects", "ProjectID", new VolleyArrayListCallback() {
                        @Override
                        public void onSuccess(ArrayList result) {
                            RESTManager.requests -= 1;
                            if (progress == 100 * RESTManager.requests && dbList.get(listIndex).getHideInMultiApp() == 0) {
                                restmanager.getStringFromJsonArrayFromUrl("https://api.itmmobile.com/projects/" + result.get(listIndex) + "/contents", "BackgroundImg", new VolleyArrayListCallback() {
                                    @Override
                                    public void onSuccess(ArrayList result) {
                                        dbList.get(listIndex).setBackgroundImg(result.get(0).toString());
                                        hashMap.put("BackgroundImg", result.get(0).toString());
                                        update();
                                    }
                                });

                                restmanager.getJsonObjectFromUrl("https://api.itmmobile.com/projects/" + result.get(listIndex), new VolleyJsonObjectCallback() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        try {
                                            dbList.get(listIndex).setAppName(result.getString("AppName"));
                                            dbList.get(listIndex).setCustomerID(Integer.parseInt(result.getString("CustomerID")));
                                            dbList.get(listIndex).setProjectDuration(result.getString("ProjectDuration"));
                                            dbList.get(listIndex).setProjectID(Integer.parseInt(result.getString("ProjectID")));
                                            hashMap.put("AppName", result.getString("AppName"));
                                            hashMap.put("CustomerID", result.getString("CustomerID"));
                                            hashMap.put("ProjectDuration", result.getString("ProjectDuration"));
                                            hashMap.put("ProjectID", result.getString("ProjectID"));
                                            update();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                if (projectIndex < 1) {
                                    requestsPerProject  = RESTManager.requests;
                                }
                            } else {
                                listIndex += 1;
                                updateRequests();
                            }
                        }
                    });
                }
            });
        } else if (!RESTManager.isMultiApp) {
            project = new Project();
            dbList.add(project);
            RESTManager.projects += 1;
            try {
                restmanager.getJsonObjectFromUrl("https://api.itmmobile.com/projects/" + code, new VolleyJsonObjectCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            dbList.get(listIndex).setProjectID(Integer.parseInt(result.getString("ProjectID")));
                            dbList.get(listIndex).setAppName(result.getString("AppName"));
                            dbList.get(listIndex).setCustomerID(Integer.parseInt(result.getString("CustomerID")));
                            dbList.get(listIndex).setProjectDuration(result.getString("ProjectDuration"));
                            hashMap.put("AppName", result.getString("AppName"));
                            hashMap.put("CustomerID", result.getString("CustomerID"));
                            hashMap.put("ProjectDuration", result.getString("ProjectDuration"));
                            hashMap.put("ProjectID", result.getString("ProjectID"));
                            update();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                restmanager.getStringFromJsonArrayFromUrl("https://api.itmmobile.com/projects/" + code + "/contents", "BackgroundImg", new VolleyArrayListCallback() {
                    @Override
                    public void onSuccess(ArrayList result) {
                        dbList.get(listIndex).setBackgroundImg(result.get(0).toString());
                        hashMap.put("BackgroundImg", project.getBackgroundImg());
                        hashMap.put("HideInMultiApp", project.getHideInMultiApp() + "");
                        update();
                    }
                });

                if (projectIndex < 1) {
                    requestsPerProject  = RESTManager.requests;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //Updates the progressbar and checks the current progress
    public void update() {
        progress += 100;
        progressBar.setProgress(progress / (RESTManager.projects * requestsPerProject));
        progressString = progress / (RESTManager.projects * requestsPerProject) + " %";
        progressView.setText(progressString);

        if (progress == 100 * RESTManager.requests && RESTManager.isMultiApp) {
            db.insertData(DatabaseHelper.EVENT_TABLE, hashMap);
            Log.d(TAG, "Project " + listIndex + ": | " + dbList.get(listIndex).getAppName()
                    + " | " + dbList.get(listIndex).getCustomerID() + " | " + dbList.get(listIndex).getProjectDuration()
                    + " | " + dbList.get(listIndex).getHideInMultiApp() + " | " + dbList.get(listIndex).getProjectID()
                    + " | " + "\n");
            listIndex += 1;
            projectIndex += 1;

            if (progress == 100 * requestsPerProject * RESTManager.projects && RESTManager.isMultiApp) {
                RESTManager.isMultiApp = false;
                Intent intent = new Intent(SplashScreen.this, ExhibitorListsbs.class);
                startActivity(intent);
            }
            if (progress != 100 * requestsPerProject * RESTManager.projects) {
                updateRequests();
            }
        } else if (progress == 100 * RESTManager.requests && !RESTManager.isMultiApp) {
            db.insertData(DatabaseHelper.EVENT_TABLE, hashMap);
            Intent intent = new Intent(SplashScreen.this, RegisterAndLogin.class);
            intent.putExtra("code", code);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

    }
}