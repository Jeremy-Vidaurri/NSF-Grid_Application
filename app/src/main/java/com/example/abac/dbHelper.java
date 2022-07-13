package com.example.abac;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/* Note: 0 for red zones
 * 1 for green zones
 * 2 for yellow zones
 */
public class dbHelper extends SQLiteOpenHelper {

    public static final String POLICY_TABLE_NAME = "Policies";
    public static final String COLUMN_NAME_POLICY_ID = "PolicyID"; // Used as a primary key for the policy table and a foreign key for matrix table
    public static final String COLUMN_NAME_POLICY_NAME = "PolicyName";
    public static final String COLUMN_NAME_WIDTH = "Width";
    public static final String COLUMN_NAME_LENGTH = "Length";

    public static final String MATRIX_TABLE_NAME = "Matrix";
    public static final String COLUMN_NAME_COLUMN_ID = "ColumnID";
    public static final String COLUMN_NAME_ROW_ID = "RowID";
    public static final String COLUMN_NAME_VALUE = "Value";

    public dbHelper(@Nullable Context context) {
        super(context, "policies.db", null, 1);
    }

    // Called on first time a database is accessed. Use this for creating a new database
    @Override
    public void onCreate(SQLiteDatabase db) {


        String SQL_CREATE_POLICIES_TABLE = "CREATE TABLE " + POLICY_TABLE_NAME + " (" +
                    COLUMN_NAME_POLICY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_POLICY_NAME + " TEXT," +
                    COLUMN_NAME_WIDTH + " INTEGER," +
                    COLUMN_NAME_LENGTH + " INTEGER" + ")";

        String SQL_CREATE_MATRIX_TABLE = "CREATE TABLE " + MATRIX_TABLE_NAME + " (" +
                    COLUMN_NAME_POLICY_ID + " INTEGER," +
                    COLUMN_NAME_COLUMN_ID + " INTEGER," +
                    COLUMN_NAME_ROW_ID + " INTEGER," +
                    COLUMN_NAME_VALUE + " INTEGER," +
                    "PRIMARY KEY ("+COLUMN_NAME_POLICY_ID+","+COLUMN_NAME_COLUMN_ID+"," + COLUMN_NAME_ROW_ID +"))";


        db.execSQL(SQL_CREATE_POLICIES_TABLE);
        db.execSQL(SQL_CREATE_MATRIX_TABLE);
    }

    // Triggers when schema is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public boolean addPolicy(PolicyModel policyModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME_POLICY_NAME, policyModel.getPolicyName());
        cv.put(COLUMN_NAME_LENGTH, policyModel.getLength());
        cv.put(COLUMN_NAME_WIDTH, policyModel.getWidth());

        long insert = db.insert(POLICY_TABLE_NAME, null, cv);

        return insert != -1;
    }

    public boolean addValue(MatrixModel matrixModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME_VALUE, matrixModel.getValue());
        cv.put(COLUMN_NAME_ROW_ID, matrixModel.getRowID());
        cv.put(COLUMN_NAME_COLUMN_ID, matrixModel.getColumnID());

        long insert = db.insert(MATRIX_TABLE_NAME, null, cv);

        return insert != -1;
    }
}
