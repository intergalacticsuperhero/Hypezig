package com.example.javaapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapp.models.Model;
import com.example.javaapp.tasks.ReadEventsFromDatabase;

import static com.example.javaapp.BaseApplication.LOG_UI;


public class FavoritesFragment extends Fragment {

    private RecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getName() + ".onCreateView() called with: inflater = ["
                + inflater + "], container = [" + container + "], savedInstanceState = ["
                + savedInstanceState + "]");

        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getName() + ".onViewCreated() called with: view = [" + view
                + "], savedInstanceState = [" + savedInstanceState + "]");

        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);

        (new ReadEventsFromDatabase(getActivity().getApplicationContext(), adapter)).execute();
    }

    private void initRecyclerView(@NonNull View view) {
        Log.d(LOG_UI, getClass().getName() + ".initRecyclerView() called with: view = ["
                + view + "]");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        adapter = new RecyclerViewAdapter(getActivity(), Model.getInstance().getFavorites());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
