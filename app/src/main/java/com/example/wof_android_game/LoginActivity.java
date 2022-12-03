package com.example.wof_android_game;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    Button sign_up_btn, login_btn, forgot_password_btn;
    ImageView imageView;
    TextView logo_tv;
    TextInputLayout username_til, password_til, email_til;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Take instance of Action Bar
        // using getSupportActionBar and
        // if it is not Null
        // then call hide function
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        Resources res = getResources();

        imageView = findViewById(R.id.minion_img);

        sign_up_btn = findViewById(R.id.login_page_sign_up_btn);
        login_btn = findViewById(R.id.login_page_submit_btn);
        forgot_password_btn = findViewById(R.id.forgot_password_btn);

        username_til = findViewById(R.id.username);
        password_til = findViewById(R.id.password);
        email_til = findViewById(R.id.email);


        sign_up_btn.setOnClickListener((view) -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(imageView, "logo_image");
            pairs[1] = new Pair<View, String>(logo_tv, "logo_text");

            //ActivityOptions activity_options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
            startActivity(intent);
        });

    }
}