package com.example.abac;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/* Note: 0 for red zones
 * 1 for green zones
 * 2 for yellow zones
 */
public class dbHelper extends SQLiteOpenHelper {

    private static final String TAG = customView.class.getSimpleName();

    public static final String POLICY_TABLE_NAME = "Policies";
    public static final String COLUMN_NAME_POLICY_ID = "PolicyID";
    public static final String COLUMN_NAME_POLICY_NAME = "PolicyName";
    public static final String COLUMN_NAME_SIZE = "Size";

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
                    COLUMN_NAME_SIZE + " INTEGER" + ")";

        String SQL_CREATE_MATRIX_TABLE = "CREATE TABLE " + MATRIX_TABLE_NAME + " (" +
                COLUMN_NAME_POLICY_ID + " INTEGER," +
                COLUMN_NAME_COLUMN_ID + " INTEGER," +
                COLUMN_NAME_ROW_ID + " INTEGER," +
                COLUMN_NAME_VALUE + " INTEGER," +
                "PRIMARY KEY (" +COLUMN_NAME_POLICY_ID+","+COLUMN_NAME_COLUMN_ID+"," +
                                COLUMN_NAME_ROW_ID +"))";


        db.execSQL(SQL_CREATE_POLICIES_TABLE);
        db.execSQL(SQL_CREATE_MATRIX_TABLE);
    }

    // Triggers when schema is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    // Add a policy to the Policies table
    public void addPolicy(PolicyModel policyModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME_POLICY_NAME, policyModel.getPolicyName());
        cv.put(COLUMN_NAME_SIZE, policyModel.getSize());

        db.insert(POLICY_TABLE_NAME, null, cv);
    }

    // Insert values into the matrix. Primarily used by initMatrix
    public void addValue(int policyID, int row, int column, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME_POLICY_ID,policyID);
        cv.put(COLUMN_NAME_VALUE, value);
        cv.put(COLUMN_NAME_ROW_ID, row);
        cv.put(COLUMN_NAME_COLUMN_ID, column);

        db.insert(MATRIX_TABLE_NAME, null, cv);
    }

    // Used to fill a matrix when a new policy is created.
    public void initMatrix(int policyID,int size) {
        for(int i =0;i<size;i++){
            for (int j=0;j<size;j++){
                addValue(policyID,i,j,1);
            }
        }
    }

    // Update a specific value within a matrix
    public void updateMatrix(int policyID, int row, int column, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME_POLICY_ID,policyID);
        cv.put(COLUMN_NAME_ROW_ID, row);
        cv.put(COLUMN_NAME_COLUMN_ID, column);
        cv.put(COLUMN_NAME_VALUE,value);

        db.update(MATRIX_TABLE_NAME,cv, "policyID="+policyID+
                " AND rowID="+row+" AND columnID="+column,null);
    }

    // Retrieve a specific value within a matrix.
    public int getValue(int policyID, int row, int column){
        SQLiteDatabase db = this.getWritableDatabase();
        int value;
        Cursor cur = db.query(MATRIX_TABLE_NAME,new String[]{COLUMN_NAME_VALUE},
                "policyID="+policyID+" AND rowID="+row+ " AND columnID="+column
                ,null,null,null,null);

        if (cur!=null){
            cur.moveToFirst();
            value = cur.getInt(0);
            cur.close();
        } else
            value = -1;

        return value;
    }

    // Delete the given policy from both tables.
    public void deletePolicy(int policyID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(POLICY_TABLE_NAME,"policyID="+policyID,null);
        db.delete(MATRIX_TABLE_NAME,"policyID="+policyID,null);
    }
}
