package com.example.eriko.updateditmmockup.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eriko.updateditmmockup.activities.EventInfobs;
import com.example.eriko.updateditmmockup.activities.SplashScreen;
import com.example.eriko.updateditmmockup.classes.Project;
import com.example.eriko.updateditmmockup.R;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Project> projectList;

    public EventListAdapter(Context context, ArrayList projectList) {
        super(context, 0, projectList);
        this.context = context;
        this.projectList = projectList;
    }

    @Override
    public int getCount() {
        return projectList.size();
    }

    @Override
    public Object getItem(int position) {
        return projectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);
        }
        ImageView banner = convertView.findViewById(R.id.banner);
        ImageView info = convertView.findViewById(R.id.info);
        ImageView download = convertView.findViewById(R.id.download);

        TextView appName = convertView.findViewById(R.id.appName);
        TextView location = convertView.findViewById(R.id.location);
        TextView duration = convertView.findViewById(R.id.duration);

        banner.setImageBitmap(stringToBitmap(projectList.get(position).getBackgroundImg()));
        info.setImageResource(R.drawable.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EventInfobs.class);
                intent.putExtra("isFromMultiApp", true);
                intent.putExtra("id", position);
                context.startActivity(intent);
            }
        });
        download.setImageResource(R.drawable.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SplashScreen.class);
                intent.putExtra("isFromMultiApp", true);
                intent.putExtra("id", position);
                context.startActivity(intent);
            }
        });

        appName.setText(projectList.get(position).getAppName());
        location.setText("Location");
        duration.setText(projectList.get(position).getProjectDuration());

        return convertView;
    }
    private Bitmap stringToBitmap(String image) {
        try {
            byte[] encodedByte = Base64.decode(image.getBytes(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}