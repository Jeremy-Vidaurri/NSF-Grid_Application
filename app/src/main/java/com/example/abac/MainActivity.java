package com.example.abac;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.List;


/* TODO:
  * Implement method to send the matrix to drone.
  * Implement a method to get current policyID.
  * Implement a screen to add a new policy.
  * Implement error checking for fields before adding a new policy.
  * Display the currently loaded policy in spinner.
  * Add a button delete the currently loaded policy. Don't allow the user to delete the last policy.
  * Implement yellow zones
*/


public class MainActivity extends AppCompatActivity {

    private customView grid;
    private dbHelper dbHelper = new dbHelper(MainActivity.this);
    private SQLiteDatabase db;
    private PolicyModel policyModel;
    private int curPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid = findViewById(R.id.grid);
        db = dbHelper.getWritableDatabase();
        updateSpinner();

        // Check if the table is empty. If so, create a 20x20 table
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + com.example.abac.dbHelper.POLICY_TABLE_NAME, null);

        if (cur!=null){
            cur.moveToFirst();
            if (cur.getInt(0)==0){
                policyModel = new PolicyModel(-1, "Example Policy",20);
                dbHelper.addPolicy(policyModel);
                dbHelper.initMatrix(1,20);
                grid.initGrid(20);
            }else{
                loadFirstPolicy();
            }
        }
        grid.invalidate();

        if (cur != null) {
            cur.close();
        }
    }


    public void loadFirstPolicy(){
        Cursor cur = db.rawQuery("SELECT MIN(PolicyID) FROM Policies",null);
        cur.moveToFirst();
        curPolicy = cur.getInt(0);
        cur.close();

        cur = db.rawQuery("SELECT size FROM Policies WHERE PolicyID=" + curPolicy,null);
        cur.moveToFirst();
        int size = cur.getInt(0);

        grid.initGrid(size);
        cur.close();
    }

    public void updateSpinner(){
        Cursor cur = db.rawQuery("SELECT PolicyID as _id, PolicyName FROM Policies",null);
        String[] orig = new String[]{"PolicyName"};
        int[] dest = new int[]{android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,cur,orig,dest,0);

        Spinner spinner = this.findViewById(R.id.spinner);
        spinner.setAdapter(sca);
    }
}