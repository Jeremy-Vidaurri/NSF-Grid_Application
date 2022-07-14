package com.example.abac;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


/* TODO:
 * Change customView.java to work with the matrix. Somehow need to pass the policyID to the grids. DO NOT create a function to get each matrix value. Just change them as you go.
 * Add a query to get the size of the matrix.
 * Finish else statement in MainActivity to load the first policy (use min() in case id 1 gets deleted.)
 * Implement a way to add more policies.
 * Spinner (?) to display the different policies
 */


public class MainActivity extends AppCompatActivity {

    private customView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper dbHelper = new dbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        PolicyModel policyModel;


        // Check if the table is empty. If so, create a 20x20 table
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + com.example.abac.dbHelper.POLICY_TABLE_NAME, null);

        if (cur!=null){
            cur.moveToFirst();
            if (cur.getInt(0)==0){
                grid = findViewById(R.id.grid);
                policyModel = new PolicyModel(-1, "Example Policy",20);
                dbHelper.addPolicy(policyModel);
                dbHelper.initMatrix(1,20);
                grid.initGrid(20);
            }
        }

        if (cur != null) {
            cur.close();
        }

        grid.invalidate();
    }
}