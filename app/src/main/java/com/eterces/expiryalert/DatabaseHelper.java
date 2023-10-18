package com.eterces.expiryalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expiry_alert.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "expiration_date";
    public static final String COLUMN_IMAGE_URI = "image_uri";
    public static final String COLUMN_CATEGORY = "category";  // Added category column

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_IMAGE_URI + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT);";  // Added category column to the table creation query

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        Log.d("MyApp", "Database created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.d("MyApp", "Database upgraded successfully");
    }

    public long insertProduct(String name, String expirationDate, String imageUri, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DATE, expirationDate);
        values.put(COLUMN_IMAGE_URI, imageUri);
        values.put(COLUMN_CATEGORY, category);  // Added category value

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();

        Log.d("MyApp", "Inserted product with ID: " + newRowId);
        return newRowId;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}