package com.kolloware.hypezigapp.ui;

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

import com.kolloware.hypezigapp.R;
import com.kolloware.hypezigapp.models.Model;
import com.kolloware.hypezigapp.models.filters.NextWeekFilter;
import com.kolloware.hypezigapp.models.filters.TodayFilter;
import com.kolloware.hypezigapp.models.filters.WeekFilter;
import com.kolloware.hypezigapp.models.filters.WeekendFilter;
import com.kolloware.hypezigapp.db.queries.QueryStrategy;
import com.kolloware.hypezigapp.db.queries.SortByCategory;
import com.kolloware.hypezigapp.db.queries.SortByDate;
import com.kolloware.hypezigapp.db.queries.SortByLocation;
import com.kolloware.hypezigapp.db.queries.SortByName;
import com.kolloware.hypezigapp.tasks.ReadEventsFromDatabase;
import com.kolloware.hypezigapp.tasks.ReloadEventsFromInternet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.kolloware.hypezigapp.BaseApplication.LOG_UI;


public class HomeFragment extends Fragment {

    private RecyclerViewAdapter adapter;

    private AlertDialog categoriesDialog;

    private SearchView searchView;

    String[] queryLabels = null;

    QueryStrategy[] queryStrategies = new QueryStrategy[]{
            new SortByDate(),
            new SortByName(),
            new SortByCategory(),
            new SortByLocation()
    };
    int queryWhich = 0;

    String[] categoryLabels = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".onCreate() called with: savedInstanceState = ["
                + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        initQueryLabels();
        initCategoryLabels();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".onCreateView() called with: inflater = ["
                + inflater + "], container = [" + container + "], savedInstanceState = ["
                + savedInstanceState + "]");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".onViewCreated() called with: view = [" + view
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
                Log.d(LOG_UI, getClass().getSimpleName() + ".onCheckedChanged() called with: "
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

        Toast.makeText(getContext(), R.string.home_swipe_to_refresh,
                Toast.LENGTH_LONG).show();
    }

    private void initRecyclerView(@NonNull View view) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".initRecyclerView() called with: view = ["
                + view + "]");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(getActivity(), Model.getInstance().getFilteredEvents());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private AlertDialog buildCategoriesDialog() {
        Log.d(LOG_UI, getClass().getSimpleName() + ".buildCategoriesDialog() called");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.category_filter_show_only);
        builder.setMultiChoiceItems(categoryLabels, null, null);
        builder.setNegativeButton(R.string.category_filter_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNeutralButton(R.string.category_filter_all, null);
        builder.setPositiveButton(R.string.category_filter_okay, null);

        return builder.create();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_UI, getClass().getSimpleName() + ".onCreateOptionsMenu() called with: menu = ["
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
        Log.d(LOG_UI, getClass().getSimpleName() + ".onOptionsItemSelected() called with: item = ["
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
        Log.d(LOG_UI, getClass().getSimpleName() + ".showAlertSortMenu() called");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.sorting_action_title);
        builder.setSingleChoiceItems(queryLabels, queryWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOG_UI, getClass().getSimpleName() + ".onClick() called with: dialog = ["
                        + dialog + "], which = [" + which + "]");
                Log.i(LOG_UI, "onClick: chosen: " + queryLabels[which]);

                queryWhich = which;
                Model.getInstance().setQueryStrategy(queryStrategies[which]);
                dialog.dismiss();

                (new ReadEventsFromDatabase(getActivity().getApplicationContext(), adapter))
                        .execute();
            }
        });
        builder.setNeutralButton(getString(R.string.sorting_action_cancel),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertFilterMenu() {
        Log.d(LOG_UI, getClass().getSimpleName() + ".showAlertFilterMenu() called");
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
        Log.d(LOG_UI, getClass().getSimpleName() + ".applySearchFilter() called");
        adapter.getFilter().filter(searchView.getQuery());
    }

    private void initQueryLabels() {
        Log.d(LOG_UI, getClass().getSimpleName() + ".initQueryLabels() called");

        queryLabels = new String[]{
                getString(R.string.query_label_time),
                getString(R.string.query_label_name),
                getString(R.string.query_label_category),
                getString(R.string.query_label_location)};
    }

    private void initCategoryLabels() {
        Log.d(LOG_UI, getClass().getSimpleName() + ".initQueryLabels() called");

        categoryLabels =  new String[]{
                getString(R.string.category_label_theatre),
                getString(R.string.category_label_cinema),
                getString(R.string.category_label_show),
                getString(R.string.category_label_party),
                getString(R.string.category_label_music),
                getString(R.string.category_label_clubbing),
                getString(R.string.category_label_dancing),
                getString(R.string.category_label_arts),
                getString(R.string.category_label_literature),
                getString(R.string.category_label_talks),
                getString(R.string.category_label_other),
                getString(R.string.category_label_family),
                getString(R.string.category_label_environs),
                getString(R.string.category_label_gastronomy),
                getString(R.string.category_label_radio),
                getString(R.string.category_label_nature)};
    }
}
