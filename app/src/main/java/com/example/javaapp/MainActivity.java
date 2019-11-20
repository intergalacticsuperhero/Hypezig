package com.example.javaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.javaapp.models.Model;
import com.example.javaapp.models.filters.PassthroughFilter;
import com.example.javaapp.models.filters.TodayFilter;
import com.example.javaapp.models.filters.WeekFilter;
import com.example.javaapp.models.filters.WeekendFilter;
import com.example.javaapp.models.queries.QueryStrategy;
import com.example.javaapp.models.queries.SortByCategory;
import com.example.javaapp.models.queries.SortByDate;
import com.example.javaapp.models.queries.SortByLocation;
import com.example.javaapp.tasks.ReadEventsFromDatabase;
import com.example.javaapp.tasks.ReloadEventsFromInternet;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerViewAdapter adapter;

    String[] queryLabels = new String[] { "Zeit", "Kategorie", "Ort" };
    QueryStrategy[] queryStrategies = new QueryStrategy[] {
            new SortByDate(),
            new SortByCategory(),
            new SortByLocation()
    };
    int queryWhich = 0;


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
        ((RadioButton) findViewById(R.id.radioButtonToday)).toggle();
        (new ReadEventsFromDatabase(getApplicationContext(), adapter)).execute();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_sort:
                showAlertSortMenu();
                break;
            default:
                System.out.println("this should never happen");
        }

        return super.onOptionsItemSelected(item);
    }


    private void showAlertSortMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Sortieren nach");
        builder.setIcon(R.drawable.icon_sort);
        builder.setSingleChoiceItems(queryLabels, queryWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("Ausgewählt: " + queryLabels[which]);
                queryWhich = which;
                Model.getInstance().setQueryStrategy(queryStrategies[which]);
                dialog.dismiss();

                (new ReadEventsFromDatabase(getApplicationContext(), adapter)).execute();
            }
        });
        builder.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

