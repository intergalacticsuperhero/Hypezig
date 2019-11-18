package com.example.javaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.javaapp.models.Event;
import com.example.javaapp.models.Location;
import com.example.javaapp.models.ScrapingResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    TextView texx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.button);

        texx = (TextView) findViewById(R.id.textView);

        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new doit().execute();
            }
        });
    }


    public class doit extends AsyncTask<Void, Void, Void> {

        String eventLabels;

        HashMap<String, Location> locations;
        HashSet<Event> events;


        @Override
        protected Void doInBackground(Void... voids) {


            try {
                ScrapingResult localResult = KreuzerScraper.fetchEvents();

                System.out.println(localResult.getEvents());
                System.out.println(localResult.getLocations());
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            texx.setText(eventLabels);
        }
    }
}

