package com.example.abac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_policy);
        Button button_back = findViewById(R.id.backButton);

        button_back.setOnClickListener(v -> startActivity(new Intent(AddPolicy.this,MainActivity.class)));
    }
}