package com.example.eriko.updateditmmockup.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.eriko.updateditmmockup.Adapters.CarouselPagerAdapter;
import com.example.eriko.updateditmmockup.Classes.Project;
import com.example.eriko.updateditmmockup.Helpers.DatabaseHelper;
import com.example.eriko.updateditmmockup.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    DatabaseHelper db;
    Cursor res;
    ArrayList<Project> dbList;

    CarouselPagerAdapter carouselPagerAdapter;
    /**
     * You shouldn't define first page = 0.
     * Let define firstpage = 'number viewpager size' to make endless carousel
     */
    public static int FIRST_PAGE = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        db = new DatabaseHelper(this);
        res = db.getAllData("Event_table");
        dbList = new ArrayList<>();

        while (res.moveToNext()) {
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