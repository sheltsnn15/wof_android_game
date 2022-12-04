package com.example.wof_android_game;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String email;
    private ArrayList<Scores> scores;

    public User(String username, String email, String password, ArrayList<Scores> scores) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.scores = scores;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
