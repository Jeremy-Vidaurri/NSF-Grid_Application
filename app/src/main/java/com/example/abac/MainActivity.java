package com.example.abac;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


/* TODO:
  * Add a button to send the matrix to drone.
  * Implement a method to get current policyID.
  * Implement a button/screen to add a new policy.
  * In adding policy screen, implement error checking for fields before adding a new policy.
  * Spinner to display/load the different policies. Display the currently loaded policy in it.
  * Add a button delete the currently loaded policy. Don't allow the user to delete the last policy.
  * Implement yellow zones
*/


public class MainActivity extends AppCompatActivity {

    private customView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid = findViewById(R.id.grid);

        dbHelper dbHelper = new dbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        PolicyModel policyModel;
        int curPolicy;


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
                cur = db.rawQuery("SELECT MIN(PolicyID) FROM Policies",null);
                cur.moveToFirst();
                curPolicy = cur.getInt(0);
                cur = db.rawQuery("SELECT size FROM Policies WHERE PolicyID=" + curPolicy,null);
                cur.moveToFirst();
                int size = cur.getInt(0);

                grid.initGrid(size);
            }
        }
        grid.invalidate();

        if (cur != null) {
            cur.close();
        }
    }
}