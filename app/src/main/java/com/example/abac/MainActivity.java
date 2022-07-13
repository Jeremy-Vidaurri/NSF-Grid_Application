package com.example.abac;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

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

                grid.initGrid(20);
                dbHelper.initMatrix(1,20);
                grid.invalidate();
            }
        }

        if (cur != null) {
            cur.close();
        }


    }
}