package com.example.abac;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


/* TODO:
  * Implement a method to get current policyID.
  * Implement a way to add more policies.
  * Spinner (?) to display the different policies.
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
        int curPolicy;


        // Check if the table is empty. If so, create a 20x20 table
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + com.example.abac.dbHelper.POLICY_TABLE_NAME, null);
        grid = findViewById(R.id.grid);
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

        if (cur != null) {
            cur.close();
        }

        grid.invalidate();
    }
}