package com.eterces.expiryalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CategoryDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "categories.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "categories";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "category_name";

    public CategoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT);";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to insert a new category
    public long insertCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, categoryName);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME, null, values);

        db.close();

        return newRowId;
    }

    // Method to retrieve all categories as a list of strings
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database to get all categories
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME}, null, null, null, null, null);

        // Iterate through the cursor and add category names to the list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                categories.add(categoryName);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return categories;
    }
}