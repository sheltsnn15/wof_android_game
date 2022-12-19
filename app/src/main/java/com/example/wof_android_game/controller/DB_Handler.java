package com.example.wof_android_game.controller;

import static java.nio.file.Files.readAllLines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wof_android_game.model.UserProfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class DB_Handler extends SQLiteOpenHelper {
    // schema of database

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "wheel_of_fortune_db";
    private static final String Table_Users = "users";
    private static final String Table_Games = "games";
    private static final String Table_Phrases = "phrases";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_PHRASES = "phrases";
    private static final String KEY_TOTAL_AMOUNT_TRIES = "total_amount_tries";
    private static final String KEY_DATE_PLAYED = "date_played";
    private static final String KEY_DIFFICULTY_LEVEL = "difficulty_level";
    private static final String PHRASES_TXT = "phrases.txt";


    public DB_Handler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE "
                + Table_Users + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT" + ")";
        String CREATE_PHRASES_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Table_Phrases + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PHRASES + " TEXT"
                + ")";
        String CREATE_GAMES_TABLE = "CREATE TABLE "
                + Table_Games + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DATE_PLAYED + " TEXT NOT NULL,"
                + KEY_TOTAL_AMOUNT_TRIES + " INTEGER,"
                + KEY_DIFFICULTY_LEVEL + " TEXT,"
                + "userID INTEGER,"
                + "phraseID INTEGER,"
                + "FOREIGN KEY(userID) REFERENCES " + Table_Users + "(" + KEY_ID + ") " +
                "ON DELETE CASCADE ON UPDATE NO ACTION,"
                + "FOREIGN KEY(phraseID) REFERENCES " + Table_Phrases + "(" + KEY_ID + ") " +
                "ON DELETE CASCADE ON UPDATE NO ACTION"
                + ")";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_PHRASES_TABLE);
        sqLiteDatabase.execSQL(CREATE_GAMES_TABLE);

        if (!checkTableIsEmpty(Table_Phrases))
            if (readPhrasesTxt()) {
                Logger.getLogger(getClass().getName()).warning("Phrases Loaded: " + PHRASES_TXT);
            }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Users);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Games);
        onCreate(sqLiteDatabase);
    }

    public boolean insertUserDetails(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_USERNAME, username);
        cv.put(KEY_EMAIL, email);
        cv.put(KEY_PASSWORD, password);

        long newRowId = db.insert(Table_Users, null, cv);
        db.close();
        // check if not successful
        return newRowId != -1;
    }

    public boolean readPhrasesTxt() {
        File file = new File(PHRASES_TXT);
        if (!file.exists()) {
            Logger.getLogger(getClass().getName()).warning("File does not exist: " + PHRASES_TXT);
            return false;
        }
        long successfulCount = 0;
        try {
            List<String> lines = readAllLines(file.toPath());
            for (String line : lines) {
                successfulCount = insertPhrases(line);
            }
            return successfulCount == lines.size();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).warning("Error reading file: " + e.getMessage());
            return false;
        }
    }

    long insertPhrases(String phrase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_PHRASES, phrase);
        // Insert the row and get the row ID of the inserted row
        long newRowId = db.insert(Table_Phrases, null, cv);
        // Close the database to free up resources
        db.close();
        return newRowId;
    }


    public int generateRandNumber(int min, int max) {
        int range = max - min + 1;
        return (int) (Math.random() * range) + min;

    }

    public String getPhrase(int index) {
        String value = "";
        String query = "SELECT * FROM " + Table_Phrases + " WHERE " + KEY_ID + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(index)});

        if (cursor.getCount() == 0) {
            // index is out of bounds
            Logger.getLogger(getClass().getName()).warning("Index " + index + " is out of bounds.");
            value = null;
        }
        if (cursor.moveToFirst()) {
            // do something with the element
            // do something with the element
            int columnIndex = cursor.getColumnIndex(KEY_PHRASES);
            // column was found, so you can retrieve its value
            value = cursor.getString(columnIndex);
        }
        db.close();
        cursor.close();
        return value;
    }

    public boolean checkTableIsEmpty(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        db.close();
        cursor.close();
        return count == 0;
    }


    public boolean checkUserName(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Table_Users, new String[]{KEY_ID}, KEY_USERNAME + "=?",
                new String[]{username}, null, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Table_Users +
                        " WHERE " + KEY_USERNAME + " = ? AND " + KEY_PASSWORD + " = ?"
                , new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        db.close();
        cursor.close();
        return exists;
    }

    public ArrayList<HashMap<String, String>> getUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        String query = "SELECT * FROM " + Table_Users;

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("username", cursor.getString(cursor.getColumnIndexOrThrow(KEY_USERNAME)));
            user.put("email", cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            user.put("password", cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD)));
            userList.add(user);
        }
        db.close();
        cursor.close();
        return userList;
    }

    public ArrayList<HashMap<String, String>> getGames(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> gamesList = new ArrayList<>();

        String query = "SELECT * FROM " + Table_Games + " WHERE " + KEY_USERNAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{username});

        while (cursor.moveToNext()) {
            HashMap<String, String> user_games = new HashMap<>();
            user_games.put("total_amount_tries", cursor.getString(cursor.getColumnIndexOrThrow(KEY_TOTAL_AMOUNT_TRIES)));
            user_games.put("difficulty_level", cursor.getString(cursor.getColumnIndexOrThrow(KEY_DIFFICULTY_LEVEL)));
            user_games.put("date_played", cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_PLAYED)));
            gamesList.add(user_games);
            UserProfile userProfile = new UserProfile(gamesList, username);
        }
        db.close();
        cursor.close();
        return gamesList;

    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Table_Users);
        db.close();
    }


}
