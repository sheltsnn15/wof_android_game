package com.example.wof_android_game;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button sign_up_btn, login_btn, forgot_password_btn;
    ImageView imageView;
    TextView logo_tv;
    TextInputLayout password_til, email_til;

    ProgressBar progressBar;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    String userID;

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

        password_til = findViewById(R.id.password);
        email_til = findViewById(R.id.email);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);


        sign_up_btn.setOnClickListener((view) -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(imageView, "logo_image");
            pairs[1] = new Pair<View, String>(logo_tv, "logo_text");

            //ActivityOptions activity_options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
            startActivity(intent);
        });

        login_btn.setOnClickListener((view) -> loginUser());

    }

    public boolean validateEmail() {
        String email = email_til.getEditText().getText().toString().trim();
        String regex = "^(.+)@(.+)$";
        if (email.isEmpty()) {
            email_til.setError("Error, Enter Email");
        } else if (!email.matches(regex)) {
            email_til.setError("Error, Invalid Email Address");
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

    public void loginUser() {

        if (!validateEmail() | !validatePassword()) {
            return;
        }

        // get values inputted by user
        String password = Objects.requireNonNull(password_til.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(email_til.getEditText()).getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}