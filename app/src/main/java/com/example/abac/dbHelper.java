package com.example.abac;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Policies";
    public static final String COLUMN_NAME_MATRIX_ID = "MatrixID";
    public static final String COLUMN_NAME_POLICY_NAME = "PolicyName";


    public dbHelper(@Nullable Context context) {
        super(context, "policies.db", null, 1);
    }

    // Called on first time a database is accessed. Use this for creating a new database
    @Override
    public void onCreate(SQLiteDatabase db) {


        String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_MATRIX_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_POLICY_NAME + " TEXT" + ")";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // Triggers when schema is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
