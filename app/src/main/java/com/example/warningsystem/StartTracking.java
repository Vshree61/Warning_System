package com.example.warningsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartTracking extends AppCompatActivity {

    Button btnStartTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tracking);
        btnStartTracking = findViewById(R.id.btn_start_tracking);

        btnStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartTracking.this, LocationActivity.class));
            }
        });
    }
}