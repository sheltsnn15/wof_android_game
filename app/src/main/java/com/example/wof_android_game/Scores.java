package com.example.wof_android_game;

public class Scores {
    private int score;
    private float wallet_balance;

    public Scores(int score, float wallet_balance) {
        this.score = score;
        this.wallet_balance = wallet_balance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(float wallet_balance) {
        this.wallet_balance = wallet_balance;
    }
}
