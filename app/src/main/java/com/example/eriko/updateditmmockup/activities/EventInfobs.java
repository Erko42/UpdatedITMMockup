package com.example.eriko.updateditmmockup.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.example.eriko.updateditmmockup.helpers.DatabaseHelper;
import com.example.eriko.updateditmmockup.classes.Project;
import com.example.eriko.updateditmmockup.R;

import java.util.ArrayList;

public class EventInfobs extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    DatabaseHelper db;
    Cursor res;
    ArrayList<Project> dbList;

    ImageView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_infobs);

        db = new DatabaseHelper(this);
        res = db.getAllData(DatabaseHelper.EVENT_TABLE);
        dbList = new ArrayList<>();

        banner = findViewById(R.id.banner);

        while (res.moveToNext()) {
            Project project = new Project(res.getString(1), res.getInt(2), res.getString(3), res.getString(4), res.getInt(5), res.getInt(6));
            dbList.add(project);
        }
        for (int i = 0; i < dbList.size(); i++) {
            Log.d(TAG, "Project " + i + ": | " + dbList.get(i).getAppName() + " | " + dbList.get(i).getCustomerID() + " | " + dbList.get(i).getProjectDuration() + " | " + dbList.get(i).getHideInMultiApp() + " | " + dbList.get(i).getProjectID() + " | " + "\n");
        }
        banner.setImageBitmap(stringToBitmap(dbList.get(getIntent().getIntExtra("id", 0)).getBackgroundImg()));
    }
    private Bitmap stringToBitmap(String image) {
        try {
            byte[] encodedByte = Base64.decode(image.getBytes(), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        if(getIntent().getExtras().getBoolean("isFromMultiApp", false)) {
            Intent intent = new Intent(EventInfobs.this, ExhibitorListsbs.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(EventInfobs.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
