package com.example.wof_android_game.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wof_android_game.R;
import com.example.wof_android_game.controller.DB_Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewGameRecordsFragment extends Fragment {

    View view;
    private DB_Handler dbHandler;
    private RecyclerView gameRecordsRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ArrayList<HashMap<String, String>> gameRecords;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_game_records, container, false);

        dbHandler = new DB_Handler(getContext());
        gameRecordsRecyclerView = view.findViewById(R.id.game_records_recycler_view);
        gameRecordsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            gameRecords = dbHandler.getGames("Shelton");
        } catch (NullPointerException e) {
            System.out.printf("Empty");
        }
        adapter = new MyRecyclerViewAdapter(getContext(), gameRecords);
        adapter.setClickListener((MyRecyclerViewAdapter.ItemClickListener) this);
        gameRecordsRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dbHandler = new DB_Handler(getActivity());
        List<Game> games = dbHandler.getAllGames();
        updateRecyclerView(games);
    }


}