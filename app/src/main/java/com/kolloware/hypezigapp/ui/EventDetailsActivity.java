package com.kolloware.hypezigapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kolloware.hypezigapp.R;
import com.kolloware.hypezigapp.db.AppDatabase;
import com.kolloware.hypezigapp.models.Event;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import static com.kolloware.hypezigapp.BaseApplication.LOG_DATA;
import static com.kolloware.hypezigapp.BaseApplication.LOG_NET;
import static com.kolloware.hypezigapp.BaseApplication.LOG_UI;

public class EventDetailsActivity extends AppCompatActivity {

    private int eventId;
    private Event event = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "E, dd.MM.yyyy 'um' HH:mm 'Uhr'");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".onCreate() called with: savedInstanceState = ["
                + savedInstanceState + "]");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle("Details");
        eventId = getIntent().getExtras().getInt("eventId");
        (new LoadEvent()).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".onOptionsItemSelected() called with: item = ["
                + item + "]");

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.item_share:
                shareEvent();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void updateViews() {
        Log.d(LOG_UI, getClass().getSimpleName() + ".updateViews() called");

        if (event == null) return;

        ((TextView) findViewById(R.id.category)).setText(event.category);
        ((TextView) findViewById(R.id.title)).setText(event.title);
        ((TextView) findViewById(R.id.subtitle)).setText(event.subtitle);
        ((TextView) findViewById(R.id.date)).setText(dateFormat.format(event.date));
        TextView location = findViewById(R.id.location);
        location.setText(Html.fromHtml("<a href='" + event.locationURL + "'>"
                + event.locationName.toUpperCase() + "</a>"));
        location.setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.details)).setText(event.details);
        ((TextView) findViewById(R.id.providerName)).setText("Quelle: " + event.providerName);

        if (event.imageURL != null) {
            new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(event.imageURL);
        }
    }

    private class LoadEvent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(LOG_DATA, getClass().getSimpleName() + ".doInBackground() called with: voids = ["
                    + voids + "]");
            event = AppDatabase.getInstance(getApplicationContext()).eventDao()
                    .getByEventId(eventId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(LOG_DATA, getClass().getSimpleName() + ".onPostExecute() called with: result = ["
                    + result + "]");
            updateViews();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private static final int THREAD_ID = 10000;

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            Log.d(LOG_NET, getClass().getSimpleName() + " constructed");

            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Log.d(LOG_NET, getClass().getSimpleName() + ".doInBackground: called with urls: "
                    + urls.toString());
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                TrafficStats.setThreadStatsTag(THREAD_ID);
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(LOG_NET, "doInBackground: ", e);
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.d(LOG_NET, getClass().getSimpleName() + ".onPostExecute() called with: result = ["
                    + result + "]");
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".onCreateOptionsMenu() called with: menu = ["
                + menu + "]");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    private void shareEvent() {
        Log.d(LOG_UI, getClass().getSimpleName() + ".shareEvent() called");
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Ich habe folgendes Event entdeckt: "
                + event.title + " am " + dateFormat.format(event.date)
                + " @ " + event.locationName;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, event.title);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Teilen ..."));
    }
}
