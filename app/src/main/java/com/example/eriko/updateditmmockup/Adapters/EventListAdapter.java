package com.example.eriko.updateditmmockup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eriko.updateditmmockup.Activities.EventInfobs;
import com.example.eriko.updateditmmockup.Classes.Project;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);
        }
        ImageView banner = (ImageView)convertView.findViewById(R.id.banner);
        ImageView info = (ImageView)convertView.findViewById(R.id.info);
        ImageView download = (ImageView)convertView.findViewById(R.id.download);

        TextView appName = (TextView)convertView.findViewById(R.id.appName);
        TextView location = (TextView)convertView.findViewById(R.id.location);
        TextView duration = (TextView)convertView.findViewById(R.id.duration);

        banner.setImageBitmap(stringToBitmap(projectList.get(position).getBackgroundImg()));
        info.setImageResource(R.drawable.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventInfobs.class);
                intent.putExtra("isFromMultiApp", true);
                intent.putExtra("id", position);
                context.startActivity(intent);
            }
        });
        download.setImageResource(R.drawable.download);

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
