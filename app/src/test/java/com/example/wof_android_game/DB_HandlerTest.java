package com.example.wof_android_game;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wof_android_game.controller.DB_Handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class DB_HandlerTest {

    private Context context;
    private DB_Handler dbHandler;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application;
        dbHandler = new DB_Handler(context);
    }

    @Test
    public void testOnCreate() {
        // Check that the database is created and tables are created with the correct schema
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        assertNotNull(db);

        // Check that the users table is created with the correct columns
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_Handler.Table_Users, null);
        String[] columnNames = cursor.getColumnNames();
        assertArrayEquals(new String[]{DB_Handler.KEY_ID, DB_Handler.KEY_USERNAME, DB_Handler.KEY_EMAIL, DB_Handler.KEY_PASSWORD}, columnNames);
        cursor.close();

        // Check that the phrases table is created with the correct columns
        cursor = db.rawQuery("SELECT * FROM " + DB_Handler.Table_Phrases, null);
        columnNames = cursor.getColumnNames();
        assertArrayEquals(new String[]{DB_Handler.KEY_ID, DB_Handler.KEY_PHRASES}, columnNames);
        cursor.close();

        // Check that the games table is created with the correct columns
        cursor = db.rawQuery("SELECT * FROM " + DB_Handler.Table_Games, null);
        columnNames = cursor.getColumnNames();
        assertArrayEquals(new String[]{DB_Handler.KEY_ID, DB_Handler.KEY_DATE_PLAYED, DB_Handler.KEY_TOTAL_AMOUNT_TRIES, DB_Handler.KEY_DIFFICULTY_LEVEL, "userID", "phraseID"}, columnNames);
        cursor.close();

        db.close();
    }

    @Test
    public void testOnUpgrade() {
        // Check that the database is upgraded and old data is not lost
    }

    @Test
    public void testInsertUserDetails() {
        // Test inserting a new user into the users table
    }

    @Test
    public void testInsertGameDetails() {
        // Test inserting a new game into the games table
    }

    @Test
    public void testGetUserByUsername() {
        // Test retrieving a user by username from the users table
    }

    @Test
    public void testGetUserByEmail() {
        // Test retrieving a user by email from the users table
    }

    @Test
    public void testGetGamesByUserId() {
        // Test retrieving games by user id from the games table
    }

    @Test
    public void testGetPhrases() {
        // Test retrieving all phrases from the phrases table
    }

    @Test
    public void testGetUsers() {
        // Test retrieving all users from the users table
    }
}
