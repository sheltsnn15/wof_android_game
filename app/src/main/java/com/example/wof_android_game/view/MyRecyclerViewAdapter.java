package com.example.wof_android_game.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wof_android_game.R;
import com.example.wof_android_game.controller.DB_Handler;
import com.example.wof_android_game.model.User;
import com.example.wof_android_game.model.WOFGame;

import java.util.ArrayList;
import java.util.HashMap;
// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

public class MyRecyclerViewAdapter extends
        RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    // parent activity will implement this method to respond to click
    private ArrayList<HashMap<String, String>> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private DB_Handler mDbHandler; // added member variable for DB_Handler object

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<HashMap<String, String>> data, DB_Handler dbHandler) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mDbHandler = dbHandler; // store DB_Handler object in member variable
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_recycler_view_layout, parent, false);
        return new ViewHolder(view, (View.OnClickListener) mClickListener);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> wofGame = mData.get(position);
        String userId = wofGame.get("userId"); // get user ID from HashMap
        User user = mDbHandler.getUser(userId); // retrieve user info from database
        String username = user.getUsername(); // get user's username from UserProfile object
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Username: " + username + "\n" +
                "Date Played: " + wofGame.getCurrentDate().toString() +
                "\nTotal Amount Tries: " + wofGame.getTotalAmountTries() +
                "\nDifficulty Level: " + wofGame.getDifficultyLevel().toString() +
                "\nPhrase: " + wofGame.getPhrase());
        holder.gameHistoryTextView.setText(stringBuilder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the user information from the data source at the specified position
        HashMap<String, String> user = mData.get(position);
        // Build the string to be displayed in the TextView
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Username: " + user.get(DB_Handler.KEY_USERNAME) + "\n");
        stringBuilder.append("Email: " + user.get(DB_Handler.KEY_EMAIL) + "\n");
        stringBuilder.append("Password: " + user.get(DB_Handler.KEY_PASSWORD) + "\n");
        // Set the text of the TextView to the built string
        holder.gameHistoryTextView.setText(stringBuilder);
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    WOFGame getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView gameHistoryTextView;
        View.OnClickListener mClickListener;

        ViewHolder(View itemView, View.OnClickListener clickListener) {
            super(itemView);
            gameHistoryTextView = itemView.findViewById(R.id.game_history_tv);
            mClickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onClick(view, getAdapterPosition());
        }
    }

}