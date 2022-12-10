package com.example.wof_android_game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DB_Handler extends SQLiteOpenHelper {
    // schema of database

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "usersdb";
    private static final String Table_Users = "userdetails";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESIGNATION = "designation";
    private static final String KEY_LOCATION = "location";


    public DB_Handler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE "
                + Table_Users + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_DESIGNATION + " TEXT,"
                + KEY_LOCATION + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Users);
        onCreate(sqLiteDatabase);
    }

    void insertUserDetails(String name, String designation, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, name);
        cv.put(KEY_DESIGNATION, designation);
        cv.put(KEY_LOCATION, location);

        long newRowId = db.insert(Table_Users, null, cv);
        db.close();
    }

    public ArrayList<HashMap<String, String>> GetUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        String query = "SELECT name, designation, location  FROM " + Table_Users;

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("name", cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
            user.put("designation", cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESIGNATION)));
            user.put("location", cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)));
            userList.add(user);
        }
        return userList;

    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Table_Users);
        db.close();
    }


}
