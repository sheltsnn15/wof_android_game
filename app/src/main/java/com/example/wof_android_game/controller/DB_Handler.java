package com.example.wof_android_game.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wof_android_game.model.UserProfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    private static final String PHRASES_TXT = "com/example/wof_android_game/phrases.txt";

    ArrayList<HashMap<String, String>> gamesList, userList;


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
        boolean isAllSucessful;
        int lineCount = 0, countReadSuccess = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(PHRASES_TXT))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                lineCount++;
                isAllSucessful = insertPhrases(line);
                if (isAllSucessful)
                    countReadSuccess++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lineCount == countReadSuccess;
    }

    boolean insertPhrases(String phrase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_PHRASES, phrase);

        long newRowId = db.insert(Table_Users, null, cv);
        db.close();
        // check if not successful
        return newRowId != -1;
    }

    public boolean getPhrase(int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table_Phrases + " WHERE " + KEY_ID + " = " + index, null);
        return cursor.getCount() > 0;
    }

    public boolean checkUserName(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table_Users +
                " WHERE " + KEY_USERNAME + " = ? ", new String[]{username});
        return cursor.getCount() > 0;
    }

    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Table_Users +
                        " WHERE " + KEY_USERNAME + " = ? AND " + KEY_PASSWORD + " = ?"
                , new String[]{username, password});
        return cursor.getCount() > 0;
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

        return gamesList;

    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Table_Users);
        db.close();
    }


}
