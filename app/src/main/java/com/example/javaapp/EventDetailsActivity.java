package com.example.javaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.javaapp.db.AppDatabase;
import com.example.javaapp.models.Event;

import java.io.InputStream;
import java.text.SimpleDateFormat;

public class EventDetailsActivity extends AppCompatActivity {

    private int eventId;
    private Event event = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd.MM.yyyy 'um' HH:mm 'Uhr'");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Details");
        eventId = getIntent().getExtras().getInt("eventId");
        (new LoadEvent()).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateViews() {
        if (event == null) return;

        ((TextView) findViewById(R.id.category)).setText(event.category);
        ((TextView) findViewById(R.id.title)).setText(event.title);
        ((TextView) findViewById(R.id.subtitle)).setText(event.subtitle);
        ((TextView) findViewById(R.id.date)).setText(dateFormat.format(event.date));
        ((TextView) findViewById(R.id.location)).setText(event.locationName.toUpperCase());
        ((TextView) findViewById(R.id.details)).setText(event.details);

        if (event.imageURL != null) {
            new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(event.imageURL);
        }
    }


    private class LoadEvent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            event = AppDatabase.getInstance(getApplicationContext()).eventDao().getByEventId(eventId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateViews();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
