package com.example.wof_android_game.model;

public enum GameErrorCodes {
    LETTER_ALREADY_GUESSED("You already guessed that letter"),
    ONLY_ONE_CHARACTER("Please enter exactly one character"),
    INVALID_INPUT("Invalid input"),
    PLAY_AGAIN("Would you like to play again? [Y/N]"),
    HAVE_WON("We have a winner!");

    private String message;

    GameErrorCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
