package com.example.wof_android_game;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String email;
    private ArrayList<Scores> scores;

    public User(String username, String password, String email, ArrayList<Scores> scores) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.scores = scores;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Scores> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Scores> scores) {
        this.scores = scores;
    }

    public void addUserScores(Scores scores) {
        this.scores.add(scores);
    }
}
