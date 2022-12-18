package com.example.wof_android_game.model;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfile {
    ArrayList<HashMap<String, String>> gamesList;
    private String userName;

    public UserProfile(ArrayList<HashMap<String, String>> gamesList, String userName) {
        this.gamesList = gamesList;
        this.userName = userName;
    }

    public UserProfile() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<HashMap<String, String>> getGamesList() {
        return gamesList;
    }

    public void setGamesList(ArrayList<HashMap<String, String>> gamesList) {
        this.gamesList = gamesList;
    }
}

