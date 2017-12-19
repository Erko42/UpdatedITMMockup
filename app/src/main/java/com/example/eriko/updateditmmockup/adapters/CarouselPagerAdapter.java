package com.example.eriko.updateditmmockup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.eriko.updateditmmockup.R;
import com.example.eriko.updateditmmockup.classes.Project;

import java.util.ArrayList;

public class CarouselPagerAdapter extends RecyclerView.Adapter<CarouselPagerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Project> data;

    public CarouselPagerAdapter(Context context, ArrayList<Project> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(data.get(position).getBackgroundImg())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        private ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.banner);
        }
    }
}