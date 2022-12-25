package com.example.wof_android_game.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wof_android_game.R;
import com.example.wof_android_game.model.DIFFICULTY_LEVEL;
import com.example.wof_android_game.model.WOFGame;

public class GameActivity extends AppCompatActivity {

    private WOFGame wofGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        wofGame = new WOFGame(DIFFICULTY_LEVEL.MEDIUM, "hello world");
    }
}