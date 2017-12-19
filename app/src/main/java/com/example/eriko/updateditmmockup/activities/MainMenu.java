package com.example.eriko.updateditmmockup.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.eriko.updateditmmockup.R;
import com.example.eriko.updateditmmockup.adapters.CarouselPagerAdapter;
import com.example.eriko.updateditmmockup.classes.Project;
import com.example.eriko.updateditmmockup.helpers.DatabaseHelper;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    DatabaseHelper db;
    Cursor res;
    ArrayList<Project> dbList;

    CarouselPagerAdapter carouselPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        db = new DatabaseHelper(this);
        res = db.getAllData(DatabaseHelper.EVENT_TABLE);
        dbList = new ArrayList<>();

        while (res.moveToNext()) {
            Log.d(TAG, "test");
            Project project = new Project(res.getString(1), res.getInt(2), res.getString(3), res.getString(4), res.getInt(5), res.getInt(6));
            dbList.add(project);
        }
        DiscreteScrollView scrollView = findViewById(R.id.picker);
        carouselPagerAdapter = new CarouselPagerAdapter(this, dbList);

        scrollView.setAdapter(carouselPagerAdapter);
        scrollView.setOffscreenItems(4);
        scrollView.setOrientation(Orientation.HORIZONTAL); //Sets an orientation of the view
    }
}