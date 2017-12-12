package com.example.eriko.updateditmmockup.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.eriko.updateditmmockup.Adapters.EventListAdapter;
import com.example.eriko.updateditmmockup.Classes.Project;
import com.example.eriko.updateditmmockup.Helpers.DatabaseHelper;
import com.example.eriko.updateditmmockup.R;

import java.util.ArrayList;

public class Tab1CurrentEvents extends Fragment {

    DatabaseHelper db;
    Cursor res;
    ArrayList<Project> dbList;

    ListView listView;
    EventListAdapter eventListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tab1_current_events, container, false);
        setHasOptionsMenu(true);
        db = new DatabaseHelper(getContext());
        res = db.getAllData("Event_table");
        dbList = new ArrayList<>();

        while (res.moveToNext()) {
            Project project = new Project(res.getString(1), res.getInt(2), res.getString(3), res.getString(4), res.getInt(5), res.getInt(6));
            dbList.add(project);
        }
        listView = (ListView)rootView.findViewById(R.id.listView);
        eventListAdapter = new EventListAdapter(getContext(), dbList);
        listView.setAdapter(eventListAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exhibitor_listsbs, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Project> searchList = new ArrayList<>();

                for (int i = 0; i < dbList.size(); i++) {
                    if(dbList.get(i).getAppName().toLowerCase().contains(newText.toLowerCase())){
                        searchList.add(dbList.get(i));
                    }
                }
                eventListAdapter = new EventListAdapter(getContext(), searchList);
                listView.setAdapter(eventListAdapter);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }
}
