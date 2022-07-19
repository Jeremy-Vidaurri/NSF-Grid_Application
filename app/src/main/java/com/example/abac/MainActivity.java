package com.example.abac;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


/* TODO:
  * Implement method to send the matrix to drone.
  * Implement a screen to add a new policy.
  * Implement error checking for fields before adding a new policy.
  * Add a button delete the currently loaded policy. Don't allow the user to delete the last policy.
  * Implement yellow zones
*/


public class MainActivity extends AppCompatActivity {

    private customView grid;
    private final dbHelper dbHelper = new dbHelper(MainActivity.this);
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
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM Policies", null);

        if (cur!=null){
            cur.moveToFirst();
            if (cur.getInt(0)==0){
                policyModel = new PolicyModel(-1, "Example Policy",20);
                dbHelper.addPolicy(policyModel);
                dbHelper.initMatrix(1,20);
                curPolicy=1;
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

    // Load the first policy. Used on initial load and when deleting the current policy.
    public void loadFirstPolicy(){
        // Select the first policyID in the table
        Cursor cur = db.rawQuery("SELECT MIN(PolicyID) FROM Policies",null);
        cur.moveToFirst();
        curPolicy = cur.getInt(0);
        cur.close();

        // Select the size from the specified policyID so that we may initialize the grid.
        cur = db.rawQuery("SELECT size FROM Policies WHERE PolicyID=" + curPolicy,null);
        cur.moveToFirst();
        int size = cur.getInt(0);

        grid.initGrid(size);
        cur.close();
    }

    // Load all policy names into the spinner and associate the id with it.
    public void updateSpinner(){
        // Select the policyID under _id so that we may use it in the spinner
        // Select Policy names so that we may display it in the spinner.
        Cursor cur = db.rawQuery("SELECT PolicyID as _id, PolicyName FROM Policies",null);
        String[] orig = new String[]{"PolicyName"};
        int[] dest = new int[]{android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,cur,orig,dest,0);

        Spinner spinner = this.findViewById(R.id.spinner);
        spinner.setAdapter(sca);
    }
}