package com.example.eriko.updateditmmockup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eriko.updateditmmockup.classes.Project;
import com.example.eriko.updateditmmockup.R;

import java.util.ArrayList;

public class CarouselPagerAdapter extends RecyclerView.Adapter<CarouselPagerAdapter.ViewHolder> {

    Context context;
    private ArrayList<Project> data;

    public CarouselPagerAdapter(Context context, ArrayList<Project> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.event_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // Glide.with(holder.itemView.getContext())
        //        .load(data.get(position).getBackgroundImg())
       //         .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            //image = (ImageView) itemView.findViewById(R.id.banner);
        }
    }
}