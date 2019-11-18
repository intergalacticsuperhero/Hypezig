package com.example.javaapp;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javaapp.tasks.ReadEventsFromDatabase;
import com.example.javaapp.tasks.ReloadEventsFromInternet;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button b1 = (Button) findViewById(R.id.button);

        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReadEventsFromDatabase(getApplicationContext()).execute();
            }
        });

        Button b2 = (Button) findViewById(R.id.button2);

        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReloadEventsFromInternet(getApplicationContext()).execute();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
}

