package com.example.javaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.javaapp.models.Model;
import com.example.javaapp.models.filters.NextWeekFilter;
import com.example.javaapp.models.filters.TodayFilter;
import com.example.javaapp.models.filters.WeekFilter;
import com.example.javaapp.models.filters.WeekendFilter;
import com.example.javaapp.models.queries.QueryStrategy;
import com.example.javaapp.models.queries.SortByCategory;
import com.example.javaapp.models.queries.SortByDate;
import com.example.javaapp.models.queries.SortByLocation;
import com.example.javaapp.models.queries.SortByName;
import com.example.javaapp.tasks.ReadEventsFromDatabase;
import com.example.javaapp.tasks.ReloadEventsFromInternet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.javaapp.BaseApplication.LOG_UI;


public class HomeFragment extends Fragment {

    private RecyclerViewAdapter adapter;

    private AlertDialog categoriesDialog;

    private SearchView searchView;

    String[] queryLabels = new String[]{"Zeit", "Name", "Kategorie", "Ort"};
    QueryStrategy[] queryStrategies = new QueryStrategy[]{
            new SortByDate(),
            new SortByName(),
            new SortByCategory(),
            new SortByLocation()
    };
    int queryWhich = 0;

    String[] categoryLabels = new String[]{"Theater", "Kino", "Show", "Party", "Musik",
            "Clubbing", "Tanzen", "Kunst", "Literatur", "Vorträge & Diskussionen", "etc.",
            "Kinder & Familie", "Umland", "Gastro-Events", "Lokale Radios", "Natur & Umwelt"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getName() + ".onCreate() called with: savedInstanceState = ["
                + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getName() + ".onCreateView() called with: inflater = ["
                + inflater + "], container = [" + container + "], savedInstanceState = ["
                + savedInstanceState + "]");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getName() + ".onViewCreated() called with: view = [" + view
                + "], savedInstanceState = [" + savedInstanceState + "]");
        super.onViewCreated(view, savedInstanceState);

        Arrays.sort(categoryLabels);

        final SwipeRefreshLayout layout = view.findViewById(R.id.refreshLayout);

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.setRefreshing(true);
                new ReloadEventsFromInternet(getActivity().getApplicationContext(), layout, adapter)
                        .execute();
            }
        });

        RadioGroup r2 = view.findViewById(R.id.radioGroupDates);
        r2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(LOG_UI, getClass().getName() + ".onCheckedChanged() called with: "
                        + "group = [" + group + "], checkedId = [" + checkedId + "]");

                switch (checkedId) {
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
                        Model.getInstance().setFilterStrategy(new NextWeekFilter());
                        break;
                    default:
                        Log.e(LOG_UI, "onCheckedChanged: invalid option: " + checkedId);
                }

                adapter.updateEventsToDisplay(Model.getInstance().getFilteredEvents());
            }
        });

        initRecyclerView(view);
        ((RadioButton) view.findViewById(R.id.radioButtonToday)).toggle();
        (new ReadEventsFromDatabase(getActivity().getApplicationContext(), adapter)).execute();

        Toast.makeText(getContext(), "Nach unten wischen um neue Events zu laden",
                Toast.LENGTH_LONG).show();
    }

    private void initRecyclerView(@NonNull View view) {
        Log.d(LOG_UI, getClass().getName() + ".initRecyclerView() called with: view = ["
                + view + "]");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(getActivity(), Model.getInstance().getFilteredEvents());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private AlertDialog buildCategoriesDialog() {
        Log.d(LOG_UI, getClass().getName() + ".buildCategoriesDialog() called");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Zeige nur");
        builder.setMultiChoiceItems(categoryLabels, null, null);
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNeutralButton("Alle", null);
        builder.setPositiveButton("OK", null);

        return builder.create();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_UI, getClass().getName() + ".onCreateOptionsMenu() called with: menu = ["
                + menu + "], inflater = [" + inflater + "]");
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(LOG_UI, getClass().getName() + ".onOptionsItemSelected() called with: item = ["
                + item + "]");

        switch (item.getItemId()) {
            case R.id.item_sort:
                showAlertSortMenu();
                break;
            case R.id.item_filter:
                showAlertFilterMenu();
                break;
            default:
                Log.e(LOG_UI, "onOptionsItemSelected: invalid option: " + item.getItemId());
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAlertSortMenu() {
        Log.d(LOG_UI, getClass().getName() + ".showAlertSortMenu() called");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Sortieren nach");
        builder.setSingleChoiceItems(queryLabels, queryWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOG_UI, getClass().getName() + ".onClick() called with: dialog = ["
                        + dialog + "], which = [" + which + "]");
                Log.i(LOG_UI, "onClick: chosen: " + queryLabels[which]);

                queryWhich = which;
                Model.getInstance().setQueryStrategy(queryStrategies[which]);
                dialog.dismiss();

                (new ReadEventsFromDatabase(getActivity().getApplicationContext(), adapter))
                        .execute();
            }
        });
        builder.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertFilterMenu() {
        Log.d(LOG_UI, getClass().getName() + ".showAlertFilterMenu() called");
        if (categoriesDialog == null) {
            categoriesDialog = buildCategoriesDialog();
        }

        categoriesDialog.show();

        categoriesDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView listView = categoriesDialog.getListView();

                boolean containsUnchecked = false;

                for (int i = 0; i < categoryLabels.length; i++) {
                    if (!listView.isItemChecked(i)) {
                        containsUnchecked = true;
                        break;
                    }
                }

                if (containsUnchecked) {
                    for (int i = 0; i < categoryLabels.length; i++) {
                        listView.setItemChecked(i, true);
                    }
                }
                else {
                    for (int i = 0; i < categoryLabels.length; i++) {
                        listView.setItemChecked(i, false);
                    }
                }

                applySearchFilter();
            }
        });

        categoriesDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ListView listView = categoriesDialog.getListView();

                Set<String> categoryLabelsSelected = new HashSet<>();
                for (int i = 0; i < categoryLabels.length; i++) {
                    if (listView.isItemChecked(i)) {
                        categoryLabelsSelected.add(categoryLabels[i]);
                    }
                }

                Model.getInstance().getCategoryFilter().setCategories(categoryLabelsSelected);
                Model.getInstance().applyFilter();
                adapter.updateEventsToDisplay(Model.getInstance().getFilteredEvents());

                categoriesDialog.dismiss();

                applySearchFilter();
            }
        });
    }

    private void applySearchFilter() {
        Log.d(LOG_UI, getClass().getName() + ".applySearchFilter() called");
        adapter.getFilter().filter(searchView.getQuery());
    }

}
