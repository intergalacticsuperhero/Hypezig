package com.example.javaapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.javaapp.models.Model;
import com.example.javaapp.models.filters.PassthroughFilter;
import com.example.javaapp.models.filters.TodayFilter;
import com.example.javaapp.models.filters.WeekFilter;
import com.example.javaapp.models.filters.WeekendFilter;
import com.example.javaapp.models.queries.SortByCategory;
import com.example.javaapp.models.queries.SortByDate;
import com.example.javaapp.models.queries.SortByLocation;
import com.example.javaapp.tasks.ReadEventsFromDatabase;
import com.example.javaapp.tasks.ReloadEventsFromInternet;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwipeRefreshLayout layout = findViewById(R.id.refreshLayout);

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.setRefreshing(true);
                new ReloadEventsFromInternet(getApplicationContext(), layout).execute();
            }
        });

        RadioGroup r = findViewById(R.id.radioGroup);
        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.radioButtonTime:
                        Model.getInstance().setQueryStrategy(new SortByDate());
                        break;
                    case R.id.radioButtonCategory:
                        Model.getInstance().setQueryStrategy(new SortByCategory());
                        break;
                    case R.id.radioButtonLocation:
                        Model.getInstance().setQueryStrategy(new SortByLocation());
                        break;
                    default:
                        System.out.println("this should never happen");
                }

                (new ReadEventsFromDatabase(getApplicationContext(), adapter)).execute();
            }
        });

        RadioGroup r2 = findViewById(R.id.radioGroupDates);
        r2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonToday:
                        Model.getInstance().setFilterStrategy(new TodayFilter());
                        break;
                    case R.id.radioButtonWeekend:
                        Model.getInstance().setFilterStrategy(new WeekendFilter());
                        break;
                    case R.id.radioButtonNextWeek:
                        Model.getInstance().setFilterStrategy(new WeekFilter());
                        break;
                    case R.id.radioButtonEverything:
                        Model.getInstance().setFilterStrategy(new PassthroughFilter());
                        break;
                    default:
                        System.out.println("this should never happen");
                }

                adapter.notifyDataSetChanged();
            }
        });

        initRecyclerView();

        ((RadioButton) findViewById(R.id.radioButtonTime)).toggle();
        ((RadioButton) findViewById(R.id.radioButtonToday)).toggle();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}

