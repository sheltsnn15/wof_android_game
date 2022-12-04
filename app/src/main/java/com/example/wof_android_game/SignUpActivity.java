package com.example.wof_android_game;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    Button sign_up_btn, login_btn;
    ImageView imageView;
    TextView logo_tv;
    TextInputLayout username_til, password_til, email_til;

    ProgressBar progressBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

        //progressBar = findViewById(R.id.progressBar);

        sign_up_btn.setOnClickListener((view) -> registerUser());


    }

    public boolean validateUserName() {
        String username = username_til.getEditText().getText().toString().trim();
        String noWhiteSpaces = "\\A\\w{4,20}\\z"; // – no white-space\n" +;
        if (username.isEmpty()) {
            username_til.setError("Error, Enter Username");
        } else if (username.length() <= 8) {
            username_til.setError("Error, Username Too Short");
        } else if (!username.matches(noWhiteSpaces)) {
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    public void registerUser() {

        if (!validateEmail() | !validateUserName() | !validatePassword()) {
            return;
        }
        // get values inputted by user
        String username = Objects.requireNonNull(username_til.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(password_til.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(email_til.getEditText()).getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        String userID = mAuth.getCurrentUser().getUid();
                        Toast.makeText(SignUpActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                });

    }
}