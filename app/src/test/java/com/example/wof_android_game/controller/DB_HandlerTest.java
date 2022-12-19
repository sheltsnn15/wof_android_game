package com.example.wof_android_game.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.File;

@RunWith(RobolectricTestRunner.class)
public class DB_HandlerTest extends TestCase {
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

    private DB_Handler dbHandler;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;
        dbHandler = new DB_Handler(context);
    }

    @Test
    public void testOnCreate() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        // Check that the users table has been created
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + Table_Users + "'", null);
        assertTrue(cursor.moveToFirst());
        cursor.close();

        // Check that the phrases table has been created
        cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + Table_Phrases + "'", null);
        assertTrue(cursor.moveToFirst());
        cursor.close();

        // Check that the games table has been created
        cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + Table_Games + "'", null);
        assertTrue(cursor.moveToFirst());
        cursor.close();
    }

    @Test
    public void testOnUpgrade() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        // Insert a dummy row into the users table
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, "testuser");
        values.put(KEY_EMAIL, "test@example.com");
        values.put(KEY_PASSWORD, "password");
        db.insert(Table_Users, null, values);

        // When
        dbHandler.onUpgrade(db, DB_VERSION, DB_VERSION + 1);

        // Then
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table_Users, null);
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    @Test
    public void testInsertUserDetails() {
        // Insert a new user into the database
        boolean result = dbHandler.insertUserDetails("test_user", "test@example.com", "test_password");
        assertTrue(result);

        // Check that the user was inserted into the database
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(Table_Users, new String[]{KEY_ID, KEY_USERNAME, KEY_EMAIL, KEY_PASSWORD},
                null, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        assertEquals("test_user", cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
        assertEquals("test@example.com", cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
        assertEquals("test_password", cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
        cursor.close();
    }

    @Test
    public void testReadPhrasesTxt() {
        // Arrange
        File file = new File(PHRASES_TXT);
        boolean expectedResult = true;

        // Act
        boolean result = dbHandler.readPhrasesTxt();

        // Assert
        assertEquals(expectedResult, result);
    }


    @Test
    public void testGenerateRandNumber() {
        // Given
        int min = 1;
        int max = 10;

        // When
        int result = dbHandler.generateRandNumber(min, max);

        // Then
        assertTrue(result >= min && result <= max);
    }

    @Test
    public void testInsertPhrases() {
        // Arrange
        String phrase = "The quick brown fox jumps over the lazy dog";
        long expectedResult = 1;

        // Act
        long result = dbHandler.insertPhrases(phrase);

        // Assert
        assertEquals(expectedResult, result);
    }


    @Test
    public void testGetPhrase() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete(Table_Phrases, null, null);  // Clear the phrases table

        // Insert a few phrases into the phrases table
        dbHandler.insertPhrases("phrase1");
        dbHandler.insertPhrases("phrase2");
        dbHandler.insertPhrases("phrase3");

        // When
        String result1 = dbHandler.getPhrase(1);
        String result2 = dbHandler.getPhrase(2);
        String result3 = dbHandler.getPhrase(3);
        String result4 = dbHandler.getPhrase(4);

        // Then
        assertEquals("phrase1", result1);
        assertEquals("phrase2", result2);
        assertEquals("phrase3", result3);
        assertNull(result4);
    }

    @Test
    public void testEmptyTable() {

        // Set up a test database with an empty table
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.execSQL("CREATE TABLE test_table (id INTEGER PRIMARY KEY, name TEXT)");

        // Call the checkTableIsEmpty method with the test table
        boolean result = dbHandler.checkTableIsEmpty("test_table");

        // Assert that the result is true (the table should be empty)
        assertTrue(result);

        // Close the database
        db.close();
    }

    @Test
    public void testNonEmptyTable() {

        // Set up a test database with a non-empty table
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.execSQL("CREATE TABLE test_table (id INTEGER PRIMARY KEY, name TEXT)");
        db.execSQL("INSERT INTO test_table (id, name) VALUES (1, 'Test')");

        // Call the checkTableIsEmpty method with the test table
        boolean result = dbHandler.checkTableIsEmpty("test_table");

        // Assert that the result is false (the table should not be empty)
        assertFalse(result);

        // Close the database
        db.close();
    }

    @Test
    public void testExistingUsername() {

        // Set up a test database with a user table containing a user with the username "test"
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, username TEXT, email TEXT, password TEXT)");
        db.execSQL("INSERT INTO users (id, username, email, password) VALUES (1, 'test', 'test@example.com', 'test123')");

        // Call the checkUserName method with the username "test"
        boolean result = dbHandler.checkUserName("test");

        // Assert that the result is true (the username should exist in the table)
        assertTrue(result);
    }

    @Test
    public void testNonExistingUsername() {

        // Set up a test database with a user table containing a user with the username "test"
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, username TEXT, email TEXT, password TEXT)");
        db.execSQL("INSERT INTO users (id, username, email, password) VALUES (1, 'test', 'test@example.com', 'test123')");

        // Call the checkUserName method with the username "other"
        boolean result = dbHandler.checkUserName("other");

        // Assert that the result is false (the username should not exist in the table)
        assertFalse(result);

        // Close the database
        db.close();
    }

}