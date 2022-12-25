package com.example.wof_android_game.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wof_android_game.R;
import com.example.wof_android_game.controller.DB_Handler;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    Button sign_up_btn, login_btn;
    ImageView imageView;
    TextView logo_tv;
    TextInputLayout username_til, password_til, email_til;

    ProgressBar progressBar;
    DB_Handler db_handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Take instance of Action Bar
        // using getSupportActionBar and
        // if it is not Null
        // then call hide function
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        imageView = findViewById(R.id.minion_img);
        username_til = findViewById(R.id.new_username);
        password_til = findViewById(R.id.new_password);
        email_til = findViewById(R.id.new_email);

        sign_up_btn = findViewById(R.id.sign_up_page_submit_btn);
        login_btn = findViewById(R.id.sign_up_page_login_btn);

        db_handler = new DB_Handler(this);
        progressBar = findViewById(R.id.progressBar);

        sign_up_btn.setOnClickListener((view) -> registerUser());


    }

    public boolean validateUserName() {
        String username = Objects.requireNonNull(username_til.getEditText()).getText().toString().trim();
        Pattern whitespace = Pattern.compile("\\s\\s");
        Matcher matcher = whitespace.matcher(username);
        if (username.isEmpty()) {
            username_til.setError("Error, Enter Username");
        } else if (username.length() <= 8) {
            username_til.setError("Error, Username Too Short");
        } else if (matcher.find()) {
            username_til.setError("Error, No White Spaces");
        } else {
            username_til.setError(null);
            username_til.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateEmail() {
        String email = email_til.getEditText().getText().toString().trim();
        String regex = "^(.+)@(.+)$";
        if (email.isEmpty()) {
            email_til.setError("Error, Enter Email");
        } else if (!email.matches(regex)) {
            email_til.setError("Error, Invalid Email Address");
        } else {
            email_til.setError(null);
            username_til.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validatePassword() {
        String password = Objects.requireNonNull(password_til.getEditText()).getText().toString().trim();
        String regex_password_validation = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        if (password.isEmpty()) {
            password_til.setError("Error, Enter Password");
        } else if (!password.matches(regex_password_validation)) {
            password_til.setError("Error, Password Too Weak");
        } else {
            password_til.setError(null);
        }
        return true;
    }

    public void registerUser() {

        if (!validateEmail() | !validateUserName() | !validatePassword()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        // get values inputted by user
        String username = Objects.requireNonNull(username_til.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(password_til.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(email_til.getEditText()).getText().toString().trim();

        Boolean checkUser = db_handler.checkUserName(username);
        if (!checkUser) {
            Boolean insert = db_handler.insertUserDetails(username, email, password);
            if (insert) {
                Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignUpActivity.this, "Sign Up Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SignUpActivity.this, "User already exists, Sign Up Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}