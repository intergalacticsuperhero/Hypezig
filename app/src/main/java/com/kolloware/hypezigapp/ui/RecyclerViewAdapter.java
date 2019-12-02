package com.kolloware.hypezigapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolloware.hypezigapp.R;
import com.kolloware.hypezigapp.db.AppDatabase;
import com.kolloware.hypezigapp.models.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.kolloware.hypezigapp.BaseApplication.LOG_APP;
import static com.kolloware.hypezigapp.BaseApplication.LOG_UI;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
        implements Filterable {

    private Context context;

    SimpleDateFormat dayOfWeek = new SimpleDateFormat("E");
    SimpleDateFormat dayAndMonth = new SimpleDateFormat("dd.MM.");
    SimpleDateFormat hours = new SimpleDateFormat("HH:mm");

    List<Event> eventsToDisplay;
    List<Event> dataSource;


    public RecyclerViewAdapter(Context context, List<Event> dataSource) {
        Log.d(LOG_APP, getClass().getSimpleName() + " constructed");

        this.context = context;
        this.dataSource = dataSource;
        this.eventsToDisplay = new ArrayList<>(dataSource);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".onCreateViewHolder() called with: parent = ["
                + parent + "], viewType = [" + viewType + "]");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,
                parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".onBindViewHolder() called with: holder = ["
                + holder + "], position = [" + position + "]");
        final Event e = eventsToDisplay.get(position);

        holder.day.setText(dayOfWeek.format(e.date).toUpperCase());
        holder.title.setText(e.title);
        holder.date.setText(dayAndMonth.format(e.date));
        holder.time.setText(hours.format(e.date));
        holder.category.setText(e.category);
        holder.location.setText(e.locationName.toUpperCase());
        holder.favorite.setImageResource(getFavoriteImageResource(e.favorite));

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_UI, getClass().getSimpleName() + ".onClick() called with: v = ["
                        + v + "]");
                e.favorite = !e.favorite;
                ((ImageButton) v).setImageResource(getFavoriteImageResource(e.favorite));

                (new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        Log.d(LOG_UI, getClass().getSimpleName() +
                                ".doInBackground() called with: voids = [" + voids + "]");
                        AppDatabase.getInstance(context).eventDao().update(e);
                        return null;
                    }
                }).execute();
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(LOG_UI, getClass().getSimpleName() + ".onClick() called with: v = ["
                        + v + "]");
                Intent intent = new Intent(context, EventDetailsActivity.class);
                Bundle b = new Bundle();
                b.putInt("eventId", e.eventId);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }


    private int getFavoriteImageResource(boolean isSelected) {
        return isSelected ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off;
    }

    @Override
    public int getItemCount() {
        return eventsToDisplay.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(LOG_UI, getClass().getSimpleName() + ".performFiltering() called with: "
                    + "constraint = [" + constraint + "]");
            List<Event> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataSource);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Event forEvent : dataSource) {
                    if (forEvent.title.toLowerCase().contains(filterPattern)
                            || forEvent.locationName.toLowerCase().contains(filterPattern)
                            || forEvent.category.toLowerCase().contains(filterPattern)
                            || forEvent.subtitle.toLowerCase().contains(filterPattern)
                            || forEvent.details.toLowerCase().contains(filterPattern)) {
                        filteredList.add(forEvent);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(LOG_UI, getClass().getSimpleName() + ".publishResults() called with: "
                    + "constraint = [" + constraint + "], results = [" + results + "]");
            eventsToDisplay.clear();
            eventsToDisplay.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView day, date, time, title, category, location;
        RelativeLayout parentLayout;
        ImageButton favorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Log.d(LOG_APP, getClass().getSimpleName() + " constructed");

            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            location = itemView.findViewById(R.id.location);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            favorite = itemView.findViewById(R.id.favorite_button);
        }
    }

    public void updateEventsToDisplay(List<Event> newList) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".updateEventsToDisplay() called with: "
                + "newList = [" + newList + "]");

        List<Event> newResults;

        if (newList != null) {
            newResults = new ArrayList<>(newList);
        }
        else {
            newResults = dataSource;
        }

        this.eventsToDisplay.clear();
        this.eventsToDisplay.addAll(newResults);

        notifyDataSetChanged();
    }
}
