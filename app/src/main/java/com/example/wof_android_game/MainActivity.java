package com.example.wof_android_game;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 5secs timers
    private static int SPLASH_SCREEN = 5000;
    Animation top_anim, bottom_anim;
    ImageView imageView;
    TextView logo_tv, slogan_tv;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Animations
        top_anim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom_anim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Take instance of Action Bar
        // using getSupportActionBar and
        // if it is not Null
        // then call hide function
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        imageView = findViewById(R.id.splash_pic_logo);
        Resources res = getResources();
        imageView.setImageResource(R.drawable.wheel_of_fortune);

        logo_tv = findViewById(R.id.logo_textview);
        slogan_tv = findViewById(R.id.slogan_textview);

        imageView.setAnimation(top_anim);
        logo_tv.setAnimation(bottom_anim);
        slogan_tv.setAnimation(bottom_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(imageView, "logo_image");
                pairs[1] = new Pair<View, String>(logo_tv, "logo_text");

                ActivityOptions activity_options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(intent, activity_options.toBundle());
            }
        }, SPLASH_SCREEN);

    }


}