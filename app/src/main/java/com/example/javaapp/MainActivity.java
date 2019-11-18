package com.example.javaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapp.models.Event;
import com.example.javaapp.tasks.ReadSortedByCategory;
import com.example.javaapp.tasks.ReadSortedByDate;
import com.example.javaapp.tasks.ReadSortedByLocation;
import com.example.javaapp.tasks.ReloadEventsFromInternet;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<Event> events = new ArrayList<>();
    private RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b2 = (Button) findViewById(R.id.button2);

        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReloadEventsFromInternet(getApplicationContext()).execute();
            }
        });


        RadioGroup r = (RadioGroup) findViewById(R.id.radioGroup);
        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonTime:
                        new ReadSortedByDate(getApplicationContext(), events, adapter).execute();
                        break;
                    case R.id.radioButtonCategory:
                        new ReadSortedByCategory(getApplicationContext(), events, adapter).execute();
                        break;
                    case R.id.radioButtonLocation:
                        new ReadSortedByLocation(getApplicationContext(), events, adapter).execute();
                        break;
                    default:
                        System.out.println("this should never happen");
                }
            }
        });

        initRecyclerView();

        ((RadioButton) findViewById(R.id.radioButtonTime)).toggle();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(events, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
}

