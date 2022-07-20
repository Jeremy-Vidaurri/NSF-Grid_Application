package com.example.abac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddPolicy extends AppCompatActivity {
    EditText fPolicyName, fSize;
    final dbHelper dbHelper = new dbHelper(this);
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_policy);
        Button button_back = findViewById(R.id.backButton);
        Button button_add_policy = findViewById(R.id.addPolicyButton);

        fPolicyName= findViewById(R.id.editPolicyName);
        fSize = findViewById(R.id.editSize);
        db = dbHelper.getWritableDatabase();

        // Functionality for back button to return to main activity
        button_back.setOnClickListener(v -> startActivity(new Intent(AddPolicy.this,MainActivity.class)));

        // When "Add Policy" button is clicked, check that the information entered is valid
        // Afterwards, add it to the database
        button_add_policy.setOnClickListener(view -> {
            if (validateSize() && validateName()){
                int size = Integer.parseInt(fSize.getText().toString().trim());
                String name = fPolicyName.getText().toString().trim();
                PolicyModel policyModel = new PolicyModel(-1,name,size);
                dbHelper.addPolicy(policyModel);
                // FIX: Add initializer for matrix
                startActivity(new Intent(AddPolicy.this,MainActivity.class));
            }
        });
    }

    // Check to see if the size is filled and within the acceptable range (5-25).
    private boolean validateSize(){
        String val = fSize.getText().toString().trim();
        if(val.isEmpty()){
            fSize.setError("Field cannot be empty");
            return false;
        } else if(Integer.parseInt(val) < 5  || Integer.parseInt(val) > 25 ){
            fSize.setError("Size must be between 5 and 25.");
            return false;
        } else {
            fSize.setError(null);
            return true;
        }
    }

    // Make sure there is at least some text in the policy name field.
    private boolean validateName(){
        String val = fPolicyName.getText().toString().trim();
        if(val.isEmpty()){
            fPolicyName.setError("Field cannot be empty");
            return false;
        } else {
            fPolicyName.setError(null);
            return true;
        }
    }

}