package com.example.javaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapp.models.Event;
import com.example.javaapp.models.Model;

import java.text.SimpleDateFormat;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;

    SimpleDateFormat dayOfWeek = new SimpleDateFormat("E");
    SimpleDateFormat dayAndMonth = new SimpleDateFormat("dd.MM.");
    SimpleDateFormat hours = new SimpleDateFormat("HH:mm");


    public RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Event e = Model.getInstance().getFilteredEvents().get(position);

        holder.day.setText(dayOfWeek.format(e.date).toUpperCase());
        holder.title.setText(e.title);
        holder.date.setText(dayAndMonth.format(e.date));
        holder.time.setText(hours.format(e.date));
        holder.category.setText(e.category);
        holder.location.setText(e.locationName.toUpperCase());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventDetailsActivity.class);
                Bundle b = new Bundle();
                b.putInt("eventId", e.eventId);
                intent.putExtras(b);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getFilteredEvents().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView day, date, time, title, category, location;
        RelativeLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            location = itemView.findViewById(R.id.location);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
