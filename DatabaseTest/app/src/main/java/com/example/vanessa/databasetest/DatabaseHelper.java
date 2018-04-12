package com.example.vanessa.databasetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vanessa.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "myDatabase";

    // Table Names
    private static final String TABLE_USER = "user";
    private static final String TABLE_RANKS = "ranks";
    private static final String TABLE_PROGRESS = "progress";

    // Common column names
    private static final String KEY_ID = "id";

    // User Table - column names
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    // Ranks Table - column names
    private static final String KEY_WEEK = "week";
    private static final String KEY_IMAGE = "image";

    // Progress Table - column names
    private static final String KEY_USER = "name";
    private static final String KEY_XVALUE = "xValues";
    private static final String KEY_YVALUE = "yValues";

    // Table Create Statements
    // User table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
            + " TEXT," + KEY_USERNAME + " TEXT," + KEY_PASSWORD
            + " TEXT" + ")";

    // Ranks table create statement
    private static final String CREATE_TABLE_RANKS = "CREATE TABLE "
            + TABLE_RANKS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_WEEK
            + " TEXT," + KEY_IMAGE + " BLOB," + ")";

    // Progress table create statement
    private static final String CREATE_TABLE_PROGRESS = "CREATE TABLE "
            + TABLE_PROGRESS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER
            + " TEXT," + KEY_XVALUE + " INTEGER," + KEY_YVALUE
            + " INTTEGER" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_RANKS);
        db.execSQL(CREATE_TABLE_PROGRESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RANKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);

        // create new tables
        onCreate(db);
    }

    public void addUser(User user) {

        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put values in  table
        values.put(KEY_NAME, user.name);
        values.put(KEY_USERNAME, user.username);
        values.put(KEY_PASSWORD, user.password);

        // insert row
        long user_id = db.insert(TABLE_USER, null, values);
    }
    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,// Selecting Table
                new String[]{KEY_ID, KEY_NAME, KEY_USERNAME, KEY_PASSWORD},//Selecting columns want to query
                KEY_USERNAME + "=?",
                new String[]{user.username},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            //if cursor has value then in user database there is user associated with this given username
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            //Match both passwords check they are same or not
            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }
        //if user password does not matches or there is no record with that username then return @false
        return null;
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,// Selecting Table
                new String[]{KEY_ID, KEY_NAME, KEY_USERNAME, KEY_PASSWORD},//Selecting columns want to query
                KEY_USERNAME + "=?",
                new String[]{username},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given username so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }


    }